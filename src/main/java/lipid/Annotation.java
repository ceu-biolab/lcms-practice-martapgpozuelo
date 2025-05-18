package lipid;

import adduct.Adduct;
import adduct.AdductList;

import java.util.*;

/**
 * Class to represent the annotation over a lipid
 */
public class Annotation {

    private final Lipid lipid;
    private final double mz;
    private final double intensity; // intensity of the most abundant peak in the groupedPeaks
    private final double rtMin;
    private final IoniationMode ionizationMode;
    private String adduct; // !!TODO The adduct will be detected based on the groupedSignals
    private final Set<Peak> groupedSignals;
    private int score;
    private int totalScoresApplied;
    private static final int PPM_TOLERANCE = 2;


    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode) {
        this(lipid, mz, intensity, retentionTime, ionizationMode, Collections.emptySet());
    }

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     * @param groupedSignals
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode, Set<Peak> groupedSignals) {
        this.lipid = lipid;
        this.mz = mz;
        this.rtMin = retentionTime;
        this.intensity = intensity;
        this.ionizationMode = ionizationMode;
        // !!TODO This set should be sorted according to help the program to deisotope the signals plus detect the adduct
        this.groupedSignals = new TreeSet<>(groupedSignals);
        this.score = 0;
        this.totalScoresApplied = 0;
    }

    public Lipid getLipid() {
        return lipid;
    }

    public double getMz() {
        return mz;
    }

    public double getRtMin() {
        return rtMin;
    }

    public String getAdduct() {
        return adduct;
    }

    public void setAdduct(String adduct) {
        this.adduct = adduct;
    }

    public double getIntensity() {
        return intensity;
    }

    public IoniationMode getIonizationMode() {
        return ionizationMode;
    }

    public Set<Peak> getGroupedSignals() {
        return Collections.unmodifiableSet(groupedSignals);
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // !CHECK Take into account that the score should be normalized between -1 and 1
    public void addScore(int delta) {
        this.score += delta;
        this.totalScoresApplied++;
    }

    /**
     * @return The normalized score between 0 and 1 that consists on the final number divided into the times that the rule
     * has been applied.
     */
    public double getNormalizedScore() {
        if (this.totalScoresApplied == 0) return 0d;
        return (double) this.score / this.totalScoresApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;
        Annotation that = (Annotation) o;
        return Double.compare(that.mz, mz) == 0 &&
                Double.compare(that.rtMin, rtMin) == 0 &&
                Objects.equals(lipid, that.lipid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lipid, mz, rtMin);
    }

    @Override
    public String toString() {
        return String.format("Annotation(%s, mz=%.4f, RT=%.2f, adduct=%s, intensity=%.1f, score=%d)",
                lipid.getName(), mz, rtMin, adduct, intensity, score);
    }

    public Map<Peak, String> getMapMZ() throws ClassNotFoundException {
        Double prevdiff = Double.MAX_VALUE;
        Peak peak1 = null;

        System.out.println("reference: " + this.mz);
        // Seleccionar el pico de referencia (más cercano al mz de la anotación)
        for (Peak candidatePeak : groupedSignals) {
            System.out.println("candidatePeak: " + candidatePeak);
            Double diff = Math.abs(this.mz - candidatePeak.getMz());
            System.out.println("diff from reference: " + diff);
            if (diff < prevdiff) {
                prevdiff = diff;
                peak1 = candidatePeak;
                System.out.println("candidate peak possible: " + peak1);
            }
            if (diff == 0) break;
        }

        Set<Peak> iterativePeaks = new LinkedHashSet<>(groupedSignals);
        iterativePeaks.remove(peak1);

        // Seleccionar mapa de aductos según el modo de ionización
        Map<String, Double> adductMap = this.ionizationMode == IoniationMode.POSITIVE
                ? AdductList.MAPMZPOSITIVEADDUCTS
                : AdductList.MAPMZNEGATIVEADDUCTS;

        // Iterar sobre combinaciones de aductos
        for (String adduct1 : adductMap.keySet()) {
            System.out.println("adduct1: " + adduct1);
            Map<String, Double> iterativeAdductMap = new LinkedHashMap<>(adductMap);
            iterativeAdductMap.remove(adduct1);

            for (String adduct2 : iterativeAdductMap.keySet()) {
                System.out.println("adduct2: " + adduct2);
                for (Peak candidatePeak2 : iterativePeaks) {
                    Double referenceMass = Adduct.getMonoisotopicMassFromMZ(this.mz, adduct1);
                    System.out.println("referenceMass: " + referenceMass);
                    Double candidateMass = Adduct.getMonoisotopicMassFromMZ(candidatePeak2.getMz(), adduct2);
                    System.out.println("candidateMass: " + candidateMass);

                    double ppm = Math.abs(Adduct.calculatePPMIncrement(referenceMass, candidateMass));
                    System.out.println("ppm: " + ppm);

                    if (ppm <= PPM_TOLERANCE) {
                        this.adduct = adduct1; //here it established the adduct that is going to be detected (principal adduct)
                        Map<Peak, String> result = new LinkedHashMap<>();
                        result.put(peak1, adduct1);
                        result.put(candidatePeak2, adduct2);
                        System.out.println("result: " + result);
                        return result;
                    }
                }
            }
        }

        throw new ClassNotFoundException("No se encontraron coincidencias de aductos para el modo de ionización: "
                + ionizationMode);
    }

}
