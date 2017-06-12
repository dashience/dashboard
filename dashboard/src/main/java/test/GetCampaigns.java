// Copyright 2016 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package test;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201609.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201609.cm.Campaign;
import com.google.api.ads.adwords.axis.v201609.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201609.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201609.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.selectorfields.v201609.cm.CampaignField;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example gets all campaigns. To add a campaign, run AddCampaign.java.
 *
 * <p>
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 */
public class GetCampaigns {

    private static final int PAGE_SIZE = 100;

    private static String clientId = "117202177181-dfpvmamitf7acnn8ginmp5d0e813u584.apps.googleusercontent.com";
    private static String clientSecret = "Z144G8Oqa15m93REF0k5z-vJ";
    private static String refreshToken = "1/o9ZysN1LtRpqKfSrkBgJ__-0GOq9FeKN_ak7WuIlI4Y";
    private static String developerToken = "zA56sMrpDz_1Hrc_AbRwyA";

    public static void main(String[] args) throws Exception {
        // Generate a refreshable OAuth2 credential.
       /* Credential oAuth2Credential = new OfflineCredentials.Builder()
         .forApi(Api.ADWORDS)
         .fromFile()
         .build()
         .generateCredential(); */
        Credential credential = new OfflineCredentials.Builder()
                .forApi(OfflineCredentials.Api.ADWORDS)
                .withClientSecrets(clientId, clientSecret)
                .withRefreshToken(refreshToken)
                .build()
                .generateCredential();

        // Construct an AdWordsSession.
        AdWordsSession session = new AdWordsSession.Builder()
                .withDeveloperToken(developerToken)
                .withClientCustomerId("588-780-5455")
                // ...
                .withOAuth2Credential(credential)
                .build();
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
        Campaign[] entries = page.getEntries();
        for (int i = 0; i < entries.length; i++) {
            Campaign entry = entries[i];
            System.out.println(entry.getName());
        }
    }

}
