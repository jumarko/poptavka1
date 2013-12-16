/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.admin;

import com.eprovement.poptavka.client.service.demand.AdminRPCService;
import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.invoice.OurPaymentDetails;
import com.eprovement.poptavka.domain.invoice.PaymentMethod;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.domain.user.rights.Permission;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.SearchConverter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.demand.PotentialDemandService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ProblemDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.demand.NewDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This RPC handles all requests for Admin module.
 * TODO LATER Martin implement all features if needed.
 * @author Martin Slavkovsky
 */
@Configurable
public class AdminRPCServiceImpl extends AutoinjectingRemoteService implements AdminRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRPCServiceImpl.class);
    private GeneralService generalService;
    private DemandService demandService;
    private MessageService messageService;
    private OfferService offerService;
    private UserMessageService userMessageService;
    private PotentialDemandService potentialDemandService;
    private Converter<Demand, FullDemandDetail> fullDemandConverter;
    private Converter<Client, ClientDetail> clientConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Offer, OfferDetail> offerConverter;
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    private Converter<ActivationEmail, ActivationEmailDetail> activationEmailConverter;
    private Converter<Invoice, InvoiceDetail> invoiceConverter;
    private Converter<OurPaymentDetails, PaymentDetail> paymentConverter;
    private Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter;
    private Converter<Permission, PermissionDetail> permissionConverter;
    private Converter<Preference, PreferenceDetail> preferenceConverter;
    private Converter<Problem, ProblemDetail> problemConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<Locality, ICatLocDetail> localityConverter;
    private Converter<Category, ICatLocDetail> categoryConverter;
    private SearchConverter searchConverter;
    private Converter<UserMessage, MessageDetail> userMessageConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
    /**************************************************************************/
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setPotentialDemandService(PotentialDemandService potentialDemandService) {
        this.potentialDemandService = potentialDemandService;
    }

    @Autowired
    public void setFullDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> fullDemandConverter) {
        this.fullDemandConverter = fullDemandConverter;
    }

    @Autowired
    public void setClientConverter(@Qualifier("clientConverter") Converter<Client, ClientDetail> clientConverter) {
        this.clientConverter = clientConverter;
    }

    @Autowired
    public void setSupplierConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setOfferConverter(@Qualifier("offerConverter") Converter<Offer, OfferDetail> offerConverter) {
        this.offerConverter = offerConverter;
    }

    @Autowired
    public void setAccessRoleConverter(
            @Qualifier("accessRoleConverter") Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        this.accessRoleConverter = accessRoleConverter;
    }

    @Autowired
    public void setActivationEmailConverter(
            @Qualifier("activationEmailConverter") Converter<
                    ActivationEmail, ActivationEmailDetail> activationEmailConverter) {
        this.activationEmailConverter = activationEmailConverter;
    }

    @Autowired
    public void setSearchConverter(@Qualifier("searchConverter") SearchConverter searchConverter) {
        this.searchConverter = searchConverter;
    }

    @Autowired
    public void setInvoiceConverter(@Qualifier("invoiceConverter") Converter<Invoice, InvoiceDetail> invoiceConverter) {
        this.invoiceConverter = invoiceConverter;
    }

    @Autowired
    public void setPaymentConverter(
            @Qualifier("paymentConverter") Converter<OurPaymentDetails, PaymentDetail> paymentConverter) {
        this.paymentConverter = paymentConverter;
    }

    @Autowired
    public void setPaymentMethodConverter(
            @Qualifier("paymentMethodConverter") Converter<PaymentMethod, PaymentMethodDetail> paymentMethodConverter) {
        this.paymentMethodConverter = paymentMethodConverter;
    }

    @Autowired
    public void setPermissionConverter(
            @Qualifier("permissionConverter") Converter<Permission, PermissionDetail> permissionConverter) {
        this.permissionConverter = permissionConverter;
    }

    @Autowired
    public void setPreferenceConverter(
            @Qualifier("preferenceConverter") Converter<Preference, PreferenceDetail> preferenceConverter) {
        this.preferenceConverter = preferenceConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, ICatLocDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    @Autowired
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, ICatLocDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setProblemConverter(@Qualifier("problemConverter") Converter<Problem, ProblemDetail> problemConverter) {
        this.problemConverter = problemConverter;
    }

    @Autowired
    public void setMessageConverter(@Qualifier("messageConverter") Converter<Message, MessageDetail> messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setUserMessageConverter(
            @Qualifier("userMessageConverter") Converter<UserMessage, MessageDetail> userMessageConverter) {
        this.userMessageConverter = userMessageConverter;
    }

    /**************************************************************************/
    /* Demand section                                                         */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminDemandsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Demand.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullDemandDetail> getAdminDemands(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Demand.class, searchDefinition);
        return fullDemandConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Boolean updateDemands(HashMap<Long, ArrayList<ChangeDetail>> changes) throws
            RPCException, ApplicationSecurityException {
        Demand demand = null;
        for (Long demandId : changes.keySet()) {
            demand = demandService.getById(demandId);
            updateDemandFields(demand, changes.get(demandId));
//            generalService.merge(demand);
            demandService.update(demand);
        }

        return true;
    }

    private Demand updateDemandFields(Demand demand, ArrayList<ChangeDetail> changes) {
        for (ChangeDetail change : changes) {
            switch (DemandField.valueOf(change.getField())) {
                case TITLE:
                    demand.setTitle((String) change.getValue());
                    break;
                case DESCRIPTION:
                    demand.setDescription((String) change.getValue());
                    break;
                case PRICE:
                    demand.setPrice(BigDecimal.valueOf(Long.valueOf((String) change.getValue())));
                    break;
                case END_DATE:
                    demand.setEndDate((Date) change.getValue());
                    break;
                case VALID_TO:
                    demand.setValidTo((Date) change.getValue());
                    break;
                case MAX_OFFERS:
                    demand.setMaxSuppliers((Integer) change.getValue());
                    break;
                case MIN_RATING:
                    demand.setMinRating(Integer.parseInt((String) change.getValue()));
                    break;
                case DEMAND_STATUS:
                    demand.setStatus(DemandStatus.valueOf((String) change.getValue()));
                    break;
                case CREATED:
                    demand.setCreatedDate((Date) change.getValue());
                    break;
                case CATEGORIES:
                    demand.setCategories(categoryConverter.convertToSourceList(
                            (ArrayList<ICatLocDetail>) change.getValue()));
                    break;
                case LOCALITIES:
                    demand.setLocalities(localityConverter.convertToSourceList(
                            (ArrayList<ICatLocDetail>) change.getValue()));
                    break;
                case EXCLUDE_SUPPLIER:
                    demand.setExcludedSuppliers(supplierConverter.convertToSourceList(
                            (ArrayList<FullSupplierDetail>) change.getValue()));
                    break;
                default:
                    break;
            }
        }
        return demand;
    }

    /**************************************************************************/
    /* Client section                                                         */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminClientsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Client.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ClientDetail> getAdminClients(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Client.class, searchDefinition);
        return clientConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateClient(ClientDetail clientDetail) throws
            RPCException, ApplicationSecurityException {
        Client client = generalService.find(Client.class, clientDetail.getId());
        if (client.getOveralRating() != clientDetail.getOveralRating()) {
            client.setOveralRating(clientDetail.getOveralRating());
        }
        client.setVerification(Verification.valueOf(clientDetail.getVerification()));
        //TODO LATER Martin - how to update businessUserData, supplierBlackList, demandsIds???
        generalService.save(client);
    }

    /**************************************************************************/
    /* Supplier sectoin                                                       */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminSuppliersCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Supplier.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<FullSupplierDetail> getAdminSuppliers(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Supplier.class, searchDefinition);
        return supplierConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateSupplier(FullSupplierDetail supplierDetail) throws
            RPCException, ApplicationSecurityException {
        Supplier supplier = generalService.find(Supplier.class, supplierDetail.getSupplierId());
        generalService.save(supplier);
    }

    /**************************************************************************/
    /* Offer sectoin                                                          */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminOffersCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Offer.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<OfferDetail> getAdminOffers(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Offer.class, searchDefinition);
        return offerConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateOffer(OfferDetail offerDetail) throws RPCException, ApplicationSecurityException {
        Offer offer = generalService.find(Offer.class, offerDetail.getId());
        if (!offer.getPrice().equals(offerDetail.getPrice())) {
            offer.setPrice((BigDecimal) offerDetail.getPrice());
        }
        if (!offer.getCreated().equals(offerDetail.getCreatedDate())) {
            offer.setCreated(offerDetail.getCreatedDate());
        }
        if (!offer.getFinishDate().equals(offerDetail.getFinishDate())) {
            offer.setFinishDate(offerDetail.getFinishDate());
        }
        if (!offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName().equals(
                offerDetail.getDisplayName())) {
            offer.getSupplier().getBusinessUser().getBusinessUserData().setCompanyName(offerDetail.getDisplayName());
        }
        OfferState newOfferState = offerService.getOfferState(offerDetail.getState().getValue());
        offer.setState(newOfferState);
        if (!offer.getPrice().equals(offerDetail.getPrice())) {
            offer.setPrice((BigDecimal) offerDetail.getPrice());
        }
        generalService.save(offer);
    }

    /**************************************************************************/
    /* Access role sectoin                                                    */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminAccessRolesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(AccessRole.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<AccessRoleDetail> getAdminAccessRoles(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(AccessRole.class, searchDefinition);
        return accessRoleConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateAccessRole(AccessRoleDetail accessRoleDetail) throws RPCException, ApplicationSecurityException {
        AccessRole accessRole = generalService.find(AccessRole.class, accessRoleDetail.getId());
        if (!accessRole.getName().equals(accessRoleDetail.getName())) {
            accessRole.setName(accessRoleDetail.getName());
        }
        if (!accessRole.getDescription().equals(accessRoleDetail.getDescription())) {
            accessRole.setDescription(accessRoleDetail.getDescription());
        }
        if (!accessRole.getCode().equalsIgnoreCase(accessRoleDetail.getCode())) {
            accessRole.setCode(accessRoleDetail.getCode());
        }
        //TODO LATER Martin - update permissions
        generalService.save(accessRole);
    }

    /**************************************************************************/
    /* Detail activation sectoin                                              */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminEmailsActivationCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(ActivationEmail.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ActivationEmailDetail> getAdminEmailsActivation(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(ActivationEmail.class, searchDefinition);
        return activationEmailConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateEmailActivation(ActivationEmailDetail emailActivationDetail) throws
            RPCException, ApplicationSecurityException {
        ActivationEmail emailActivation = generalService.find(ActivationEmail.class, emailActivationDetail.getId());
        if (!emailActivation.getActivationCode().equals(emailActivationDetail.getActivationCode())) {
            emailActivation.setActivationCode(emailActivationDetail.getActivationCode());
        }
        if (!emailActivation.getValidTo().equals(emailActivationDetail.getTimeout())) {
            emailActivation.setValidTo(emailActivationDetail.getTimeout());
        }
        generalService.save(emailActivation);
    }

    /**************************************************************************/
    /* Invoice sectoin                                                        */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminInvoicesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Invoice.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<InvoiceDetail> getAdminInvoices(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Invoice.class, searchDefinition);
        return invoiceConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateInvoice(InvoiceDetail invoiceDetail) throws RPCException, ApplicationSecurityException {
        Invoice invoice = generalService.find(Invoice.class, invoiceDetail.getId());
        if (!invoice.getInvoiceNumber().equals(invoiceDetail.getInvoiceNumber())) {
            invoice.setInvoiceNumber(invoiceDetail.getInvoiceNumber());
        }
        //------------------------------ Dates ---------------------------------
        if (!invoice.getIssueDate().equals(invoiceDetail.getIssueDate())) {
            invoice.setIssueDate(invoiceDetail.getIssueDate());
        }
        if (!invoice.getShipmentDate().equals(invoiceDetail.getShipmentDate())) {
            invoice.setShipmentDate(invoiceDetail.getShipmentDate());
        }
        if (!invoice.getDueDate().equals(invoiceDetail.getDueDate())) {
            invoice.setDueDate(invoiceDetail.getDueDate());
        }
        //------------------------ Bank information ----------------------------
        if (!invoice.getBankAccountNumber().equals(invoiceDetail.getBankAccountNumber())) {
            invoice.setBankAccountNumber(invoiceDetail.getBankAccountNumber());
        }
        if (!invoice.getBankCode().equals(invoiceDetail.getBankCode())) {
            invoice.setBankCode(invoiceDetail.getBankCode());
        }
        if (!invoice.getVariableSymbol().equals(invoiceDetail.getVariableSymbol())) {
            invoice.setVariableSymbol(invoiceDetail.getVariableSymbol());
        }
        if (!invoice.getConstSymbol().equals(invoiceDetail.getConstSymbol())) {
            invoice.setConstSymbol(invoiceDetail.getConstSymbol());
        }
        //------------------------------ Price ---------------------------------
        if (!invoice.getTaxBasis().equals(invoiceDetail.getTaxBasis())) {
            invoice.setTaxBasis(invoiceDetail.getTaxBasis());
        }
        if (invoice.getVatRate() != invoiceDetail.getVatRate()) {
            invoice.setVatRate(invoiceDetail.getVatRate());
        }
        if (!invoice.getVat().equals(invoiceDetail.getVat())) {
            invoice.setVat(invoiceDetail.getVat());
        }
        if (!invoice.getTotalPrice().equals(invoiceDetail.getTotalPrice())) {
            invoice.setTotalPrice(invoiceDetail.getTotalPrice());
        }
        if (!invoice.getConstSymbol().equals(invoiceDetail.getConstSymbol())) {
            invoice.setConstSymbol(invoiceDetail.getConstSymbol());
        }
        //TODO LATER Martin - how to update userServices, paymentMethods
        generalService.save(invoice);
    }

    /**************************************************************************/
    /* Message sectoin                                                        */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminMessagesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Message.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<MessageDetail> getAdminMessages(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Message.class, searchDefinition);
        return messageConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateMessage(MessageDetail messageDetail) throws RPCException, ApplicationSecurityException {
        Message message = generalService.find(Message.class, messageDetail.getMessageId());
        //TODO LATER Martin - how to update missing ones
        if (!message.getSubject().equals(messageDetail.getSubject())) {
            message.setSubject(messageDetail.getSubject());
        }
        if (!message.getBody().equals(messageDetail.getBody())) {
            message.setBody(messageDetail.getBody());
        }
        if (!message.getCreated().equals(messageDetail.getCreated())) {
            message.setCreated(messageDetail.getCreated());
        }
        if (!message.getSent().equals(messageDetail.getSent())) {
            message.setSent(messageDetail.getSent());
        }
        if (!message.getMessageState().equals(MessageState.valueOf(messageDetail.getMessageState()))) {
            message.setMessageState(MessageState.valueOf(messageDetail.getMessageState()));
        }

        generalService.save(message);
    }

    /**************************************************************************/
    /*  NEW DEMANDS SECTION.                                                  */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminDemandsByItsStatusCount(
            SearchDefinition searchDefinition, DemandStatus demandStatus) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Demand.class, searchDefinition);
        search.addFilterEqual("status", demandStatus);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<NewDemandDetail> getAdminDemandsByItsStatus(
            SearchDefinition searchDefinition, DemandStatus demandStatus) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Demand.class, searchDefinition);
        search.addFilterEqual("status", demandStatus);
        final List<Demand> demands = generalService.search(search);
        final List<NewDemandDetail> demandDetails = new ArrayList<NewDemandDetail>();
        for (Demand demand : demands) {
            NewDemandDetail detail = new NewDemandDetail();
            detail.setSenderId(demand.getClient().getBusinessUser().getId());
            detail.setCreated(demand.getCreatedDate());
            detail.setDemandId(demand.getId());
            detail.setLocalities(localityConverter.convertToTargetList(demand.getLocalities()));
            detail.setThreadRootId(messageService.getThreadRootMessage(demand).getId());
            detail.setDemandTitle(demand.getTitle());
            detail.setValidTo(demand.getValidTo());
            demandDetails.add(detail);
        }
        return demandDetails;
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void approveDemands(Set<NewDemandDetail> demandsToApprove) throws
            RPCException, ApplicationSecurityException {
        LOGGER.info("action=approve_demands status=start");
        for (NewDemandDetail demandDetail : demandsToApprove) {
            try {
                final Demand demand = demandService.getById(demandDetail.getDemandId());
                demandService.activateDemand(demand);
                potentialDemandService.sendDemandToPotentialSuppliers(demand);
            } catch (Exception e) {
                LOGGER.warn("action=approve_demands status=error demand_id={}", demandDetail.getDemandId(), e);
            }

        }
        LOGGER.info("action=approve_demands status=start");
    }

    /**************************************************************************/
    /* Our payment detail sectoin                                             */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminOurPaymentDetailsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(OurPaymentDetails.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentDetail> getAdminOurPaymentDetails(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(OurPaymentDetails.class, searchDefinition);
        return paymentConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateOurPaymentDetail(PaymentDetail paymentDetail) throws RPCException, ApplicationSecurityException {
        OurPaymentDetails ourPaymentDetails = generalService.find(OurPaymentDetails.class, paymentDetail.getId());
        if (!ourPaymentDetails.getBankAccount().equals(paymentDetail.getBankAccount())) {
            ourPaymentDetails.setBankAccount(paymentDetail.getBankAccount());
        }
        if (!ourPaymentDetails.getBankCode().equals(paymentDetail.getBankCode())) {
            ourPaymentDetails.setBankCode(paymentDetail.getBankCode());
        }
        if (!ourPaymentDetails.getCity().equals(paymentDetail.getCity())) {
            ourPaymentDetails.setCity(paymentDetail.getCity());
        }
        if (!ourPaymentDetails.getCountryVat().equals(paymentDetail.getCountryVat())) {
            ourPaymentDetails.setCountryVat(paymentDetail.getCountryVat());
        }
        if (!ourPaymentDetails.getEmail().equals(paymentDetail.getEmail())) {
            ourPaymentDetails.setEmail(paymentDetail.getEmail());
        }
        if (!ourPaymentDetails.getIban().equals(paymentDetail.getIban())) {
            ourPaymentDetails.setIban(paymentDetail.getIban());
        }
        if (!ourPaymentDetails.getIdentificationNumber().equals(paymentDetail.getIdentificationNumber())) {
            ourPaymentDetails.setIdentificationNumber(paymentDetail.getIdentificationNumber());
        }
        if (!ourPaymentDetails.getPhone().equals(paymentDetail.getPhone())) {
            ourPaymentDetails.setPhone(paymentDetail.getPhone());
        }
        if (!ourPaymentDetails.getStreet().equals(paymentDetail.getStreet())) {
            ourPaymentDetails.setStreet(paymentDetail.getStreet());
        }
        if (!ourPaymentDetails.getSwiftCode().equals(paymentDetail.getSwiftCode())) {
            ourPaymentDetails.setSwiftCode(paymentDetail.getSwiftCode());
        }
        if (!ourPaymentDetails.getTaxId().equals(paymentDetail.getTaxId())) {
            ourPaymentDetails.setTaxId(paymentDetail.getTaxId());
        }
        if (!ourPaymentDetails.getTitle().equals(paymentDetail.getTitle())) {
            ourPaymentDetails.setTitle(paymentDetail.getTitle());
        }
        if (!ourPaymentDetails.getZipCode().equals(paymentDetail.getZipCode())) {
            ourPaymentDetails.setZipCode(paymentDetail.getZipCode());
        }
        generalService.save(ourPaymentDetails);
    }

    /**************************************************************************/
    /* Payment methods sectoin                                                */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPaymentMethodsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(PaymentMethod.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentMethodDetail> getAdminPaymentMethods(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(PaymentMethod.class, searchDefinition);
        return paymentMethodConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PaymentMethodDetail> getAdminPaymentMethods() throws
            RPCException, ApplicationSecurityException {
        final Search search = new Search(PaymentMethod.class);
        search.addSort("id", false);
        return paymentMethodConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePaymentMethod(PaymentMethodDetail paymentMethodDetail) throws
            RPCException, ApplicationSecurityException {
        PaymentMethod paymentMethod = generalService.find(PaymentMethod.class, paymentMethodDetail.getId());
        if (!paymentMethod.getName().equals(paymentMethodDetail.getName())) {
            paymentMethod.setName(paymentMethodDetail.getName());
        }
        if (!paymentMethod.getDescription().equals(paymentMethodDetail.getDescription())) {
            paymentMethod.setDescription(paymentMethodDetail.getDescription());
        }
        generalService.save(paymentMethod);
    }

    /**************************************************************************/
    /* Permission sectoin                                                     */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPermissionsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Permission.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PermissionDetail> getAdminPermissions(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Permission.class, searchDefinition);
        return permissionConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePermission(PermissionDetail permissionDetail) throws RPCException, ApplicationSecurityException {
        Permission permission = generalService.find(Permission.class, permissionDetail.getId());
        if (!permission.getName().equals(permissionDetail.getName())) {
            permission.setName(permissionDetail.getName());
        }
        if (!permission.getDescription().equals(permissionDetail.getDescription())) {
            permission.setDescription(permissionDetail.getDescription());
        }
        if (!permission.getCode().equals(permissionDetail.getCode())) {
            permission.setCode(permissionDetail.getCode());
        }
        generalService.save(permission);
    }

    /**************************************************************************/
    /* Preference sectoin                                                     */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminPreferencesCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Preference.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<PreferenceDetail> getAdminPreferences(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Preference.class, searchDefinition);
        return preferenceConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updatePreference(PreferenceDetail preferenceDetail) throws RPCException, ApplicationSecurityException {
        Preference preference = generalService.find(Preference.class, preferenceDetail.getId());
        if (!preference.getKey().equals(preferenceDetail.getKey())) {
            preference.setKey(preferenceDetail.getKey());
        }
        if (!preference.getValue().equals(preferenceDetail.getValue())) {
            preference.setValue(preferenceDetail.getValue());
        }
        if (!preference.getDescription().equals(preferenceDetail.getDescription())) {
            preference.setDescription(preferenceDetail.getDescription());
        }
        generalService.save(preference);
    }

    /**************************************************************************/
    /* Problem sectoin                                                        */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long getAdminProblemsCount(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Problem.class, searchDefinition);
        return (long) generalService.count(search);
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public List<ProblemDetail> getAdminProblems(SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException {
        Search search = searchConverter.convertToSource(Problem.class, searchDefinition);
        return problemConverter.convertToTargetList(generalService.search(search));
    }

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public void updateProblem(ProblemDetail problemDetail) throws RPCException, ApplicationSecurityException {
        Problem problem = generalService.find(Problem.class, problemDetail.getId());
        if (!problem.getText().equals(problemDetail.getText())) {
            problem.setText(problemDetail.getText());
        }
        generalService.save(problem);
    }

    /**
     * This method will update number of unread messages of logged user.
     * Since this RPC class requires access of authenticated user (see security-web.xml) this method will be called
     * only when PoptavkaUserAuthentication object exist in SecurityContextHolder and we can retrieve userId.
     *
     * @return UnreadMessagesDetail with number of unread messages and other info to be displayed after users logs in
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException {
        Long userId = ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        Search unreadMessagesSearch = new Search(UserMessage.class);
        unreadMessagesSearch.addFilterNotNull("message.demand");
        unreadMessagesSearch.addFilterEqual("isRead", false);
        unreadMessagesSearch.addFilterEqual("user.id", userId.longValue());
        unreadMessagesSearch.addField("id", Field.OP_COUNT);
        unreadMessagesSearch.setResultMode(Search.RESULT_SINGLE);
        UnreadMessagesDetail unreadMessagesDetail = new UnreadMessagesDetail();
        unreadMessagesDetail.setUnreadMessagesCount((
                (Long) generalService.searchUnique(unreadMessagesSearch)).intValue());
        Search unreadSystemMessagesSearch = new Search(UserMessage.class);
        unreadSystemMessagesSearch.addFilterNull("message.demand");
        unreadSystemMessagesSearch.addFilterEqual("isRead", false);
        unreadSystemMessagesSearch.addFilterEqual("user.id", userId.longValue());
        unreadSystemMessagesSearch.addField("id", Field.OP_COUNT);
        unreadSystemMessagesSearch.setResultMode(Search.RESULT_SINGLE);
        unreadMessagesDetail.setUnreadSystemMessageCount((
                (Long) generalService.searchUnique(unreadSystemMessagesSearch)).intValue());
        return unreadMessagesDetail;
    }

    /**
     * Gets all inbox and sent user messages UsereMessage of given user and thread root.
     * Once loaded, all user messages are set as read, see isRead attribute of UserMessage.
     *
     * @param threadRootId is root message id
     * @param loggedUserId can be either supplier's or client's user Id
     * @return list of all messages in this thread
     * @throws RPCException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<MessageDetail> getConversation(long threadRootId, long loggedUserId, long counterPartyUserId)
        throws RPCException, ApplicationSecurityException {
        final List<UserMessage> userMessages = getConversationUserMessages(
                threadRootId, loggedUserId, counterPartyUserId);
        return userMessageConverter.convertToTargetList(userMessages);
    }

    private List<UserMessage> getConversationUserMessages(long threadRootId, long loggedUserId,
            long counterPartyUserId) {
        Message threadRoot = messageService.getById(threadRootId);

        User user = this.generalService.find(User.class, loggedUserId);
        User counterparty = this.generalService.find(User.class, counterPartyUserId);
        return userMessageService.getConversation(user, counterparty, threadRoot);
    }

    /**
     * Creates conversation between <code>Client</code> and Admin/Operator user. Conversation is created in such a
     * way that new <code>UserMessage</code> is created for every <code>User</code> who invokes this method. Thus
     * enabling the user to write a reply message to <code>Client</code> in order to update <code>Demand</code>
     * description or title before this demand is approved.
     *
     * @param demandId for which the conversation is created
     * @param userAdminId id of operator or admin user
     * @return thread root id
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public Long createConversation(long demandId, long userAdminId) throws RPCException,
        ApplicationSecurityException {
        Demand demand = demandService.getById(demandId);
        Message threadRootMessage = messageService.getThreadRootMessage(demand);
        userMessageService.getAdminUserMessage(threadRootMessage, generalService.find(User.class, userAdminId));
        return threadRootMessage.getId();
    }

}
