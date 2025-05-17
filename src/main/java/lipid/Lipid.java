package lipid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Lipid {
    private final int compoundId;
    private final String name;
    private final String formula;
    private final LipidType lipidType; // !! OPTIONAL TODO -> TRANSFORM INTO AN ENUMERATION
    private final int carbonCount;
    private final int doubleBondsCount;


    /**
     * @param compoundId
     * @param name
     * @param formula
     * @param lipidType
     * @param carbonCount
     * @param doubleBondCount
     */
    public Lipid(int compoundId, String name, String formula, LipidType lipidType, int carbonCount, int doubleBondCount) {
        this.compoundId = compoundId;
        this.name = name;
        this.formula = formula;
        this.lipidType = lipidType;
        this.carbonCount = carbonCount;
        this.doubleBondsCount = doubleBondCount;
    }

    public int getCompoundId() {
        return compoundId;
    }

    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public LipidType getLipidType() {
        return this.lipidType;
    }

    public int getCarbonCount() {
        return carbonCount;
    }

    public int getDoubleBondsCount() {
        return doubleBondsCount;
    }

    public Integer comparelipid(LipidType lipidType2) {
        /// Valor 2: error el lipido no está en datos
        /// Valor 1: evidencia positiva
        /// Valor 0: evidencia negativa
        Map<LipidType,Integer> lipidPriority = new LinkedHashMap<>();
        lipidPriority.put(LipidType.PG, 0);lipidPriority.put(LipidType.PE, 1);lipidPriority.put(LipidType.PI, 2);lipidPriority.put(LipidType.PA, 3);lipidPriority.put(LipidType.PS, 4);lipidPriority.put(LipidType.PC, 5);
        if (lipidType2 == null || this.lipidType == null) {
            System.err.println("Uno de los tipos de lípido es nulo.");
            return 2;
        }
        Integer priority1 = lipidPriority.get(this.lipidType);
        Integer priority2 = lipidPriority.get(lipidType2);
        System.out.println("Comparando: " + this.lipidType + " (" + priority1 + ") vs " + lipidType2 + " (" + priority2 + ")");
        if (priority1 == null || priority2 == null) {
            System.err.println("Uno de los tipos de lípido no está en la lista válida.");
            return 2;
        }
        if (priority1 > priority2){
            System.out.println("Esta comprobacion da bien");
            return 1;
        }else{
            System.out.println("Esta comprobacion da mal");
            return 0;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lipid)) return false;
        Lipid lipid = (Lipid) o;
        return compoundId == lipid.compoundId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(compoundId);
    }

    @Override
    public String toString() {
        return "Lipid{" +
                "compoundId=" + compoundId +
                ", name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", lipidType='" + lipidType + '\'' +
                ", carbonCount=" + carbonCount +
                ", doubleBondCount=" + doubleBondsCount +
                '}';
    }


}
