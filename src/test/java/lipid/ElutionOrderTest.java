package lipid;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ElutionOrderTest {


    static final Logger LOG = LoggerFactory.getLogger(ElutionOrderTest.class);

    // !!TODO For the adduct detection both regular algorithms or drools can be used as far the tests are passed.


    @Before
    public void setup() {
        // !! TODO Empty by now,you can create common objects for all tests.
    }


    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of carbons if the lipid type and the number of
     * double bonds is the same. The larger the number of carbons, the longer the RT.
     */
    @Test
    public void score1BasedOnRT() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);


        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/
        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", LipidType.TG, 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", LipidType.TG, 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IoniationMode.POSITIVE);


        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }

    }

    @Test
    public void score1BasedOnRTCarbonNumbers() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", LipidType.TG, 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", LipidType.TG, 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }

    }

    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     */
    @Test
    public void score1BasedOnRTDoubleBonds() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 54:4", "C57H102O6", LipidType.TG, 54, 4); // MZ of [M+H]+ = 883.77492
        Lipid lipid3 = new Lipid(3, "TG 54:2", "C57H106O6", LipidType.TG, 54, 2); // MZ of [M+H]+ = 887.80622
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 883.77492, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 887.80622, 10E5, 11d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }
    }

    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     * The RT order of lipids with the same number of carbons and double bonds is the same
     * -> PG < PE < PI < PA < PS << PC.
     */
    @Test
    public void score1BasedOnLipidType() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.PI, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", LipidType.PG, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }
    }

    // !! TODO. PART II OF THE PRACTICE. The negative evidence.
    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     * The RT order of lipids with the same number of carbons and double bonds is the same
     * -> PG < PE < PI < PA < PS << PC.
     */
    @Test
    public void negativeScoreBasedOnRTNumberOfCarbons() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.PI, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", LipidType.PG, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }


    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     */
    @Test
    public void negativeScoreBasedOnRTDoubleBonds() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 54:4", "C57H102O6", LipidType.TG, 54, 4); // MZ of [M+H]+ = 883.77492
        Lipid lipid3 = new Lipid(3, "TG 54:2", "C57H106O6", LipidType.TG, 54, 2); // MZ of [M+H]+ = 887.80622
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 883.77492, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 887.80622, 10E5, 8d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of carbons if the lipid type and the number of
     * double bonds is the same. The larger the number of carbons, the longer the RT.
     */
    @Test
    public void negativeScoreBasedOnLipidType() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", LipidType.TG, 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", LipidType.TG, 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1


            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself


        }
        finally {
            instance.close();
        }

    }

    @Test
    public void negativeScoreBasedOnNoExistentLipidType() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.FA, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", LipidType.PG, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IoniationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }

    @Test
    public void score1BasedOnLipidTypeMultiple() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", LipidType.PG, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PE 34:0", "C40H79O10P", LipidType.PE, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PI 34:0", "C42H84NO8P", LipidType.PI, 54, 0); // MZ of [M+H]+ = 762.60073
        Lipid lipid4 = new Lipid(4, "PA 34:0", "C43H83O13P", LipidType.PA, 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid5 = new Lipid(5, "PS 34:0", "C40H79O10P", LipidType.PS, 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid6 = new Lipid(6, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0); // MZ of [M+H]+ = 762.60073
        Lipid lipid7 = new Lipid(7, "PC 34:0", "C42H84NO8P", LipidType.PC, 54, 0);
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IoniationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 11d, IoniationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 12d, IoniationMode.POSITIVE);
        Annotation annotation4 = new Annotation(lipid4, 839.5644179056, 10E6, 13d, IoniationMode.POSITIVE);
        Annotation annotation5 = new Annotation(lipid5, 751.54836, 10E7, 14d, IoniationMode.POSITIVE);
        Annotation annotation6 = new Annotation(lipid6, 913.822, 10E5, 17d, IoniationMode.POSITIVE);
        Annotation annotation7 = new Annotation(lipid7, 913.822, 10E5, 1d, IoniationMode.POSITIVE);
        //the only one that is wrongly placed is annotation 7 bc its RT is the lowest one, and should be the highest one.

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);
            lipidScoreUnit.getAnnotations().add(annotation4);
            lipidScoreUnit.getAnnotations().add(annotation5);
            lipidScoreUnit.getAnnotations().add(annotation6);
            lipidScoreUnit.getAnnotations().add(annotation7);
            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertTrue("My message here", annotation7.getNormalizedScore() < -0.5d);
            System.out.println("score for annotation 7 : " + annotation7.getNormalizedScore());//its value is -1 bc it is compared with the rest of annotations (1,2,3,4,5,6) and it does not fulfill
            //the elution irder (PC is the highest and it has the lowest RT) --> -1-1-1-1-1-1 = -6/6=-1
            assertTrue("Condition fulfilled", annotation1.getNormalizedScore() < 0.7d);//it is 0.6666..., as there are 6 rules fulfilled (annotation 1 w 2,3,4,5,6 (score 1)
            // and annotation 7 (score -1) --> 1+1+1+1+1-1 = 4/6=0.6666...
            System.out.println("score for annotation 1: " + annotation1.getNormalizedScore());
            assertTrue("Condition fulfilled", annotation2.getNormalizedScore() < 0.7d);
            System.out.println("score for annotation 2: " + annotation2.getNormalizedScore());
            assertTrue("Condition fulfilled", annotation3.getNormalizedScore() < 0.7d);
            System.out.println("score for annotation 3: " + annotation3.getNormalizedScore());
            assertTrue("Condition fulfilled", annotation4.getNormalizedScore() < 0.7d);
            System.out.println("score for annotation 4: " + annotation4.getNormalizedScore());
            assertTrue("Condition fulfilled", annotation5.getNormalizedScore() < 0.7d);
            System.out.println("score for annotation 5: " + annotation5.getNormalizedScore());
            assertEquals(1.0, annotation6.getNormalizedScore(), 0.01);//the score is 1.0 bc when annotation 6 is compared w annotation 7, no rule is triggered since no rule fulfills
            //that condition. Therefore, there are only 5 positive comparisons and 5 comparisons in total --> 5/5 = 1.0
//            assertTrue("Condition fulfilled", annotation6.getNormalizedScore() < 0.7d);
            System.out.println("score for annotation 6: " + annotation6.getNormalizedScore());

        }
        finally {
            instance.close();
        }
    }

}
