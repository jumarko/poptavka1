package cz.poptavka.sample.client;

public final class NameTokens {

    public static final String DEMANDS_PAGE = "demandsPage";
    public static final String SIGN_IN_PAGE = "signInPage";
    public static final String MAIN_PAGE = "mainPage";
    public static final String ERROR_PAGE = "errorPage";


    public static String getSignInPage() {
        return SIGN_IN_PAGE;
    }

    public static String getMainPage() {
        return MAIN_PAGE;
    }

    private NameTokens() {
        // DO NOT instantiate utility class
    }
}
