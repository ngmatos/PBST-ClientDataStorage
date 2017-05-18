package org.feup.ses.pbst.patternTests.informationDisclosure;

import org.feup.ses.pbst.Enums.PatternEnum;
import org.feup.ses.pbst.TestConfAndResult;
import org.feup.ses.pbst.patternTests.FormValuesHolder;
import org.feup.ses.pbst.patternTests.WebPage;
import org.feup.ses.pbst.tests.*;

public class ClientDataStorage extends InformationDisclosure {

    public void startTests(WebPage webPage, TestConfAndResult testConfAndResult, FormValuesHolder formValuesHolder){
        ClientDataStorageImpl clientDataStorage = new ClientDataStorageImpl();
        clientDataStorage.test(webPage, testConfAndResult, formValuesHolder, PatternEnum.INFORMATION_DISCLOSURE_CDS);
    }
}
