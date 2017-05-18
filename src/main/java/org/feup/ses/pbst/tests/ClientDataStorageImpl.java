package org.feup.ses.pbst.tests;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.feup.ses.pbst.Enums.PatternEnum;
import org.feup.ses.pbst.Enums.TestResultEnum;
import org.feup.ses.pbst.TestConfAndResult;
import org.feup.ses.pbst.patternTests.FormValuesHolder;
import org.feup.ses.pbst.patternTests.WebPage;

import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientDataStorageImpl extends Test {

    private final String COOKIE_HEADER_RESPONSE = "Set-Cookie";
    private final String COOKIE_HEADER_REQUEST = "Cookie";

    public ClientDataStorageImpl() { super(); }

    public void test(WebPage webPage, TestConfAndResult testConfAndResult, FormValuesHolder formValuesHolder,
                     PatternEnum informationDisclosureCds) {

        super.setWebPage(webPage);
        super.setPbstTest(testConfAndResult);

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        try {
            CookieHandler.setDefault(manager);
            URLConnection connection = webPage.getUrl().openConnection();
            connection.getContent();
        } catch (Exception ex) {

        }

        CookieStore cookieJar =  manager.getCookieStore();
        List <HttpCookie> cookies = cookieJar.getCookies();

        for (HttpCookie cookie : cookies) {
            if (getShannonEntropy(cookie.getValue()) < 3.6){
                webPage.addTestResult(informationDisclosureCds, TestResultEnum.VULNERABLE,
                        "Client Data Storage", cookie.getName() + " might be vulnerable or have a weak crypto algorithm");
            } else {
                webPage.addTestResult(informationDisclosureCds, TestResultEnum.SECURE,
                        "Client Data Storage", cookie.getName() + " is probably secured or have a strong crypto algorithm");
            }
        }
    }

    private double getShannonEntropy(String s) {
        int n = 0;
        Map<Character, Integer> occ = new HashMap<>();

        for (int c_ = 0; c_ < s.length(); ++c_) {
            char cx = s.charAt(c_);
            if (occ.containsKey(cx)) {
                occ.put(cx, occ.get(cx) + 1);
            } else {
                occ.put(cx, 1);
            }
            ++n;
        }

        double e = 0.0;
        for (Map.Entry<Character, Integer> entry : occ.entrySet()) {
            char cx = entry.getKey();
            double p = (double) entry.getValue() / n;
            e += p * log2(p);
        }
        return -e;
    }

    private double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    private boolean hasCookie(HttpResponse response){

        return Arrays.stream(response.getAllHeaders())
                .map(Header::getName)
                .anyMatch((header) -> header.equals(COOKIE_HEADER_RESPONSE));
    }

    private String getCookies(HttpResponse response){

        return Arrays.stream(response.getAllHeaders())
                .filter(header -> header.getName().equals(COOKIE_HEADER_RESPONSE))
                .map(header -> header.getValue().split(";")[0])
                .collect(Collectors.joining("; "));
    }

}
