/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.datasource;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Selector;
import static com.visumbu.vb.datasource.GaDataSource.getAllDataSets;
import java.io.IOException;
import java.util.List;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class AdWordsDataSource extends BaseDataSource {

    @Override
    public List getDataSets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getDataDimensions(String dataSetName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List getDataDimensions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static void main(String argv[]) {

        try {
            /**
             * Create an AdWordsSession instance, loading credentials from the
             * properties file:
             */
            // Get an OAuth2 credential.
            /*Credential credential = new OfflineCredentials.Builder()
                    .forApi(OfflineCredentials.Api.ADWORDS)
                    .fromFile()
                    .build()
                    .generateCredential();
            
            // Construct an AdWordsSession.
            AdWordsSession session = new AdWordsSession.Builder()
                    .fromFile()
                    .withOAuth2Credential(credential)
                    .build(); */
            
            String clientId = "162577857765-le844eg61vkr4rejv4br7grl3cplvn9h.apps.googleusercontent.com";
            String clientSecret = "n3ZXN2YOLwcWx7HZk4AmIq66";
            String refreshToken = "";
            String developerToken = "X4glgfA7zjlwzeL3jNQjkw";
            /**
             * Alternatively, you can specify your credentials in the constructor:
             */
            // Get an OAuth2 credential.
            Credential credential = new OfflineCredentials.Builder()
                    .forApi(OfflineCredentials.Api.ADWORDS)
                    .withClientSecrets(clientId, clientSecret)
                   // .withRefreshToken(refreshToken)
                    .build()
                    .generateCredential();
            
            // Construct an AdWordsSession.
            AdWordsSession session = new AdWordsSession.Builder()
                    .withDeveloperToken(developerToken)
                    // ...
                    .withOAuth2Credential(credential)
                    .build();
            
            /**
             * Instantiate the desired service class using the AdWordsServices
             * utility and a Class object representing your service.
             */
            // Get the CampaignService.
            CampaignServiceInterface campaignService
                    = new AdWordsServices().get(session, CampaignServiceInterface.class);
            
            /**
             * Create data objects and invoke methods on the service class instance.
             * The data objects and methods map directly to the data objects and
             * requests for the corresponding web service.
             */
            // Create selector.
            Selector selector = new Selector();
            selector.setFields(new String[]{"Id", "Name"});
            
            // Get all campaigns.
            CampaignPage page = campaignService.get(selector);
        } catch (OAuthException ex) {
            Logger.getLogger(AdWordsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            Logger.getLogger(AdWordsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(AdWordsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
