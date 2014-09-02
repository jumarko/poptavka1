/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandCategory;
import com.eprovement.poptavka.domain.demand.DemandLocality;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.SupplierCategory;
import com.eprovement.poptavka.domain.user.SupplierLocality;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserDataField;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.FullClientDetail.ClientField;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail.OfferField;
import com.eprovement.poptavka.shared.domain.demand.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail.MessageField;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail.UserMessageField;
import com.eprovement.poptavka.shared.domain.supplier.SupplierField;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Contains all used mapping in search process. In client we can select which
 * attributes should be searched or sorted. We can also define in what class
 * is particular attribute present. But we don't know nothing on client side about
 * RPC implementation - which method, which domain class used in genericdao.Search.
 * Therefore we are not able to provide path for attributes which depends on search class.
 *
 * To do the process of sorting and searching automatically , this class provide
 * mapping of various search classes and domain objects used. Lets say our RPC method
 * searches in <b>UserMessages</b>. And we want sorting client rating attribute. We know
 * that the attribute is present in <b>Client</b> class. Therefore this class has mapping:
 * If search class is <b>UserMessage</b> and we want to sort some <b>client</b>'s attribute,
 * use path <b>message.demand.client.</b> to access those attributes.
 *
 * @author Martin Slavkovsky
 */
public final class DomainObjectsMaping {

    private ArrayList<MapingPair> mapingPairs = new ArrayList<MapingPair>();
    //Singleton instance
    private static DomainObjectsMaping instacne;

    public static DomainObjectsMaping getInstance() {
        if (instacne == null) {
            return new DomainObjectsMaping();
        }
        return instacne;
    }

    /**
     * Initialize mapping.
     */
    private DomainObjectsMaping() {
        //Demand
        mapingPairs.add(new MapingPair(Demand.class, DemandField.class, ""));
        mapingPairs.add(new MapingPair(DemandCategory.class, DemandField.class, "demand."));
        mapingPairs.add(new MapingPair(DemandLocality.class, DemandField.class, "demand."));
        //Client
        mapingPairs.add(new MapingPair(Client.class, ClientField.class, ""));
        mapingPairs.add(new MapingPair(Client.class, UserField.class, "businessUser."));
        mapingPairs.add(new MapingPair(Client.class, UserDataField.class, "businessUser.businessUserData."));
        //Supplier
        mapingPairs.add(new MapingPair(Supplier.class, SupplierField.class, ""));
        mapingPairs.add(new MapingPair(Supplier.class, UserDataField.class, "businessUser.businessUserData."));
        mapingPairs.add(new MapingPair(SupplierCategory.class, SupplierField.class, "supplier."));
        mapingPairs.add(new MapingPair(SupplierLocality.class, SupplierField.class, "supplier."));
        //Offer
        mapingPairs.add(new MapingPair(Offer.class, DemandField.class, "demand."));
        //UserMessage
        mapingPairs.add(new MapingPair(UserMessage.class, DemandField.class, "message.demand."));
        mapingPairs.add(new MapingPair(UserMessage.class, ClientField.class, "message.demand.client."));
        mapingPairs.add(new MapingPair(UserMessage.class, SupplierField.class, "message.offer.supplier."));
        mapingPairs.add(new MapingPair(UserMessage.class, OfferField.class, "message.offer."));
        mapingPairs.add(new MapingPair(UserMessage.class, MessageField.class, "message."));
        mapingPairs.add(new MapingPair(UserMessage.class, UserMessageField.class, ""));
        mapingPairs.add(new MapingPair(UserMessage.class, UserDataField.class, "businessUser.businessUserData."));
    }

    /**
     *
     * @param searchClass - class used in com.google.genericdao.Search object.
     * @param enumClass - enum class defining search attributes.
     * @return
     */
    public String getPath(Class<?> searchClass, Class<?> enumClass) {
        int idx = mapingPairs.indexOf(new MapingPair(searchClass, enumClass, null));
        if (idx != -1) {
            return mapingPairs.get(idx).getPath();
        }
        return "";
    }
}

/**
 * Represents maping pair.
 * @author Martin Slavkovsky
 */
class MapingPair {

    /**
     * searchClass and searchClassAttributes builds unique identifications.
     */
    private Class<?> searchClass;
    private Class<?> enumClass;
    private String path;

    public MapingPair(Class<?> searchClass, Class<?> enumClass, String path) {
        this.searchClass = searchClass;
        this.enumClass = enumClass;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.searchClass);
        hash = 97 * hash + Objects.hashCode(this.enumClass);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MapingPair other = (MapingPair) obj;
        if (!Objects.equals(this.searchClass, other.searchClass)) {
            return false;
        }
        if (!Objects.equals(this.enumClass, other.enumClass)) {
            return false;
        }
        return true;
    }
}
