/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.util.messaging.demand;

import com.eprovement.crawldemands.demand.Demand;

/**
 *
 * @author ivan.vlcek
 */
public final class TestingDemand {

    public static final String TEST_DEMAND_1_TITLE =
            "Popt\u00e1v\u00e1m:  piliny pod kon\u011b, cca 160 m3/rok, S\u00e1zava, okr. Bene\u0161ov";

    public static final String TEST_DEMAND_1_CONTACT_PERSON = "Miroslav Tup\u00fd";
    public static final String TEST_DEMAND_1_EMAIL = "sup@test.cz5";
    public static final String TEST_DEMAND_1_PHONE = "+420 603 569 481";
    public static final String TEST_DEMAND_1_COMPANY = "St\u00e1j B\u011blokozly v.o.s";
    public static final String TEST_DEMAND_1_DIC = "CZ27406351";
    public static final String TEST_DEMAND_1_IC = "27406351";
    public static final String TEST_DEMAND_1_LINK = "http://www.epoptavka.cz/index.php?section=2&p=78dae3c8267d2f86";
    public static final String TEST_DEMAND_1_DESCRIPTION = "Popis popt\u00e1vky: koup\u00edm piliny pod kon\u011b";
    public static final String TEST_DEMAND_1_CITY = "Praha 5";
    public static final String TEST_DEMAND_1_STREET = "Gran\u00e1tov\u00e1 554";


    private TestingDemand() {
    }

    public static Demand[] generateDemands() {
        return new Demand[] {
                generateDemand(),
                generateDemand2()
        };
    }

    private static Demand generateDemand() {
        Demand demand = new Demand();
        demand.setAttractive(null);
        demand.setCategory("d\u0159evo");
        demand.setCity(TEST_DEMAND_1_CITY);
        demand.setClientDescription("");
        demand.setCompany(TEST_DEMAND_1_COMPANY);
        demand.setContactPerson(TEST_DEMAND_1_CONTACT_PERSON);
        demand.setDateOfCreation("15.04. 2011");
        demand.setDescription(TEST_DEMAND_1_DESCRIPTION);
        demand.setDic(TEST_DEMAND_1_DIC);
        demand.setEmail(TEST_DEMAND_1_EMAIL);
        demand.setIco(TEST_DEMAND_1_IC);
        demand.setLink(TEST_DEMAND_1_LINK);
        demand.setLinkToCommercialRegister(null);
        demand.setLocality("kraj vyso\u010dina");
        demand.setLocalityId("10");
        demand.setName(TEST_DEMAND_1_TITLE);
        demand.setOriginalHtml("");
        demand.setPhone(TEST_DEMAND_1_PHONE);
        demand.setStreet(TEST_DEMAND_1_STREET);
        demand.setWww(null);
        return demand;
    }

    private static Demand generateDemand2() {
        Demand demand = new Demand();
        demand.setAttractive(null);
        demand.setCategory("d\u0159evo");
        demand.setCity(TEST_DEMAND_1_CITY);
        demand.setClientDescription("");
        demand.setCompany(TEST_DEMAND_1_COMPANY);
        demand.setContactPerson("Miroslav Tup\u00fd");
        demand.setDateOfCreation("15.04. 2011");
        demand.setDescription(TEST_DEMAND_1_DESCRIPTION);
        demand.setDic(TEST_DEMAND_1_DIC);
        demand.setEmail("mail@domena.cz");
        demand.setIco(TEST_DEMAND_1_IC);
        demand.setLink(TEST_DEMAND_1_LINK);
        demand.setLinkToCommercialRegister(null);
        demand.setLocality("slovensko");
        demand.setLocalityId(null);
        demand.setName("Popt\u00e1v\u00e1m:  piliny pod kon\u011b, cca 160 m3/rok, S\u00e1zava, okr. Bene\u0161ov");
        demand.setOriginalHtml("");
        demand.setPhone(TEST_DEMAND_1_PHONE);
        demand.setStreet(TEST_DEMAND_1_STREET);
        demand.setWww(null);
        return demand;
    }
}
