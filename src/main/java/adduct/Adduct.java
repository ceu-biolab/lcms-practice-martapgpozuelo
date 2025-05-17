package adduct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adduct {
// !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).

    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param mz mz
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return the monoisotopic mass of the experimental mass mz with the adduct @param adduct
     */
    /**
     * Calculates the monoisotopic mass from the experimental m/z and the adduct.
     */
    //!TODO method
    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct) {
        Double adductMass = null;

        if (AdductList.MAPMZPOSITIVEADDUCTS.containsKey(adduct)) {
            adductMass = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        } else if (AdductList.MAPMZNEGATIVEADDUCTS.containsKey(adduct)) {
            adductMass = AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        } else {
            System.err.println("Adduct no reconocido: " + adduct);
            return null;
        }

        int charge = extractCharge(adduct);
        System.out.println(adduct + " has a charge of " + charge);
        int multimerCount = extractMultimer(adduct);
        System.out.println(adduct + " has a multimer count of " + multimerCount);
        double monoisotopicMass = (mz * charge + adductMass) / multimerCount;
        return monoisotopicMass;
    }



    /**
     * Calculate the mz of a monoisotopic mass with the corresponding adduct
     *
     * @param monoisotopicMass
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return
     */
    /**
     * Calculates the m/z from the monoisotopic mass and the adduct.
     */
    // !! TODO method
    // !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).

    public static Double getMZFromMonoisotopicMass(Double monoisotopicMass, String adduct) {
        Double adductMass = null;

        if (AdductList.MAPMZPOSITIVEADDUCTS.containsKey(adduct)) {
            adductMass = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        } else if (AdductList.MAPMZNEGATIVEADDUCTS.containsKey(adduct)) {
            adductMass = AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        } else {
            System.err.println("Adduct no reconocido: " + adduct);
            return null;
        }

        int charge = extractCharge(adduct);
        int multimerCount = extractMultimer(adduct);

        double mz = ((monoisotopicMass * multimerCount) - adductMass / charge);
        return mz;
    }


    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass Mass measured by MS
     * @param theoreticalMass  Theoretical mass of the compound
     */
    public static int calculatePPMIncrement(Double experimentalMass, Double theoreticalMass) {
        return (int) Math.round(Math.abs((experimentalMass - theoreticalMass) * 1_000_000 / theoreticalMass));
    }


    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param measuredMass    Mass measured by MS
     * @param ppm ppm of tolerance
     */
    public static double calculateDeltaPPM(Double experimentalMass, int ppm) {
        return Math.round(Math.abs((experimentalMass * ppm) / 1_000_000));
    }


    private static int extractCharge(String adduct) {
        Pattern chargePattern = Pattern.compile("](\\d+)?[+-−]");
        Matcher matcher = chargePattern.matcher(adduct);
        if (matcher.find()) {
            String value = matcher.group(1);
            return (value != null) ? Integer.parseInt(value) : 1;
        }
        return 1;
    }


    private static int extractMultimer(String adduct) {
        Pattern multimerPattern = Pattern.compile("(\\d+)M");
        Matcher matcher = multimerPattern.matcher(adduct);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 1;
    }
}