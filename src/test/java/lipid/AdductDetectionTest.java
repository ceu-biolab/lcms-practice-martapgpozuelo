package lipid;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdductDetectionTest {
    // !!TODO For the adduct detection both regular algorithms or drools can be used as far the tests are passed.


    @Before
    public void setup() {
        // !! TODO Empty by now,you can create common objects for all tests.
    }

    @Test
    public void shouldDetectAdductBasedOnMzDifference() {

        // Given two peaks with ~21.98 Da difference (e.g., [M+H]+ and [M+Na]+)
        Peak mH = new Peak(700.500, 100000.0); // [M+H]+
        Peak mNa = new Peak(722.482, 80000.0);  // [M+Na]+
        Lipid lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", LipidType.PC, 34, 1);

        double annotationMZ = 700.49999d;
        double annotationIntensity = 80000.0;
        double annotationRT = 6.5d;
        Annotation annotation = new Annotation(lipid, annotationMZ, annotationIntensity, annotationRT, IoniationMode.POSITIVE, Set.of(mH, mNa));

        try{
            Map<Peak,String> maparelacciones= annotation.getMapMZ();
            System.out.println("resultado final: " + maparelacciones);
        }catch(Exception e){
            System.out.println("aducto no encontrado");
        }

        // Then we should call the algorithmic/knowledge system rules fired to detect the adduct and Set it!
        //
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }


    @Test
    public void shouldDetectLossOfWaterAdduct() {
        Peak mh = new Peak(700.500, 90000.0);        // [M+H]+
        Peak mhH2O = new Peak(682.4894, 70000.0);     // [M+H–H₂O]+, ~18.0106 Da less

        Lipid lipid = new Lipid(1, "PE 36:2", "C41H78NO8P", LipidType.PE, 36, 2);
        Annotation annotation = new Annotation(lipid, mh.getMz(), mh.getIntensity(), 7.5d, IoniationMode.POSITIVE, Set.of(mh, mhH2O));

        try{
            Map<Peak,String> maparelacciones= annotation.getMapMZ();
        }catch(Exception e){
            System.out.println("aducto no encontrado");
        }

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }

    @Test
    public void shouldDetectDoublyChargedAdduct() {
        // Assume real M = (700.500 - 1.0073) = 699.4927
        // So [M+2H]2+ = (M + 2.0146) / 2 = 350.7536
        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.754, 85000.0);   // [M+2H]2+

        Lipid lipid = new Lipid(3, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3);
        Annotation annotation = new Annotation(lipid, singlyCharged.getMz(), singlyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));

        try{
            Map<Peak,String> maparelacciones= annotation.getMapMZ();
        }catch(Exception e){
            System.out.println("aducto no encontrado");
        }

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }



    @Test
    public void shouldDetectNegativeAdductBasedOnMzDifference() {

        Peak mH = new Peak(700.500, 100000.0);     // [M-H]−
        Peak mCl = new Peak(736.4767, 80000.0);    // [M+Cl]−

        Lipid lipid = new Lipid(2, "PE 36:2", "C41H78NO8P", LipidType.PE, 36, 2);

        double annotationMZ = 700.500;
        double annotationIntensity = 95000.0;
        double annotationRT = 5.3;

        Annotation annotation = new Annotation(
                lipid,
                annotationMZ,
                annotationIntensity,
                annotationRT,
                IoniationMode.NEGATIVE,
                Set.of(mH, mCl)
        );

        try {
            Map<Peak, String> adductMatches = annotation.getMapMZ();
            adductMatches.forEach((peak, adduct) ->
                    System.out.printf("Matched peak %.4f with adduct %s%n", peak.getMz(), adduct));
        } catch (Exception e) {
            System.out.println("aducto no encontrado");
        }

        assertNotNull("[M-H]- should be detected", annotation.getAdduct());
        assertEquals("[M-H]−", annotation.getAdduct());
    }



    @Test
    public void shouldDetectNegativeAdductHCOOHvsH2O() {
        // Pico más bajo: [M-H-H2O]−
        Peak mH2O = new Peak(700.500, 90000.0);       // [M-H-H2O]−
        Peak mHCOOH = new Peak(764.5166, 85000.0);    // [M+HCOOH-H]−

        Lipid lipid = new Lipid(3, "PG 32:0", "C38H74O10P", LipidType.PG, 32, 0);

        double annotationMZ = 700.500;
        double annotationIntensity = 88000.0;
        double annotationRT = 4.2;

        Annotation annotation = new Annotation(
                lipid,
                annotationMZ,
                annotationIntensity,
                annotationRT,
                IoniationMode.NEGATIVE,
                Set.of(mH2O, mHCOOH)
        );

        try {
            Map<Peak, String> adductMatches = annotation.getMapMZ();
            adductMatches.forEach((peak, adduct) ->
                    System.out.printf("Matched peak %.4f with adduct %s%n", peak.getMz(), adduct));
        } catch (Exception e) {
            System.out.println("aducto no encontrado");
        }

        assertNotNull("Se debería detectar el aducto negativo", annotation.getAdduct());
        assertEquals("[M-H-H2O]−", annotation.getAdduct());
    }




}
