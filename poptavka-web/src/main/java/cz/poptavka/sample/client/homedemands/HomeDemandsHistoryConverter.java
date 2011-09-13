package cz.poptavka.sample.client.homedemands;
public class HomeDemandsHistoryConverter {

//    private static final Logger LOGGER = Logger.getLogger(HomeDemandsHistoryConverter.class.getName());
//
//    public String convertToToken(String tokenName) {
//        return tokenName;
//    }
//    public String convertToToken(String historyName, CategoryDetail category) {
//        return Long.toString(category.getId());
//    }
//
//    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
//
//    @Override
//    public void convertFromToken(String historyName, String param, HomeDemandsEventBus eventBus) {
//        eventBus.setHistoryStoredForNextOne(false);
//        eventBus.displayMenu();
//
//        if (historyName.equals("atHome")) {
//            eventBus.atHome();
//        }
//        if (historyName.equals("addToPath")) {
//            eventBus.loadingShow(MSGS.loading());
//            if (param.equals("root")) {
//                eventBus.atSuppliers();
//            } else {
//                eventBus.setCategoryID(Long.valueOf(param));
//                eventBus.removeFromPath(Long.valueOf(param));
//                eventBus.getSubCategories(Long.valueOf(param));
//            }
//        }
//        if (historyName.equals("atCreateDemand")) {
//            eventBus.atCreateDemand();
//        }
//        if (historyName.equals("atDemands")) {
//            eventBus.atDemands();
//        }
//        if (historyName.equals("atSuppliers")) {
//            eventBus.atSuppliers();
//        }
//        if (historyName.equals("atRegisterSupplier")) {
//            eventBus.atRegisterSupplier();
//        }
//    }
//
//    @Override
//    public boolean isCrawlable() {
//        // TODO Auto-generated method stub
//        return true;
//    }
}
