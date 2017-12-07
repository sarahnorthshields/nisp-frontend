/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.nisp.helpers

import uk.gov.hmrc.http.cache.client.SessionCache
import uk.gov.hmrc.nisp.config.{ApplicationConfig, ApplicationGlobalTrait}
import uk.gov.hmrc.nisp.controllers.StatePensionController
import uk.gov.hmrc.nisp.controllers.connectors.CustomAuditConnector
import uk.gov.hmrc.nisp.services.{MetricsService, NationalInsuranceService, StatePensionService}
import uk.gov.hmrc.nisp.utils.MockTemplateRenderer
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.partials.CachedStaticHtmlPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer

object MockStatePensionController extends MockStatePensionController {
  override val citizenDetailsService = MockCitizenDetailsService
  override implicit val cachedStaticHtmlPartialRetriever: CachedStaticHtmlPartialRetriever = MockCachedStaticHtmlPartialRetriever
  override implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer
}

object MockMWRREStatePensionController extends MockStatePensionController {
  override val citizenDetailsService = MockCitizenDetailsService
  override implicit val cachedStaticHtmlPartialRetriever: CachedStaticHtmlPartialRetriever = MockCachedStaticHtmlPartialRetriever
  override implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer

  override val statePensionService: StatePensionService = MockStatePensionServiceViaStatePension
  override val nationalInsuranceService: NationalInsuranceService = MockNationalInsuranceServiceViaNationalInsurance
}

trait MockStatePensionController extends StatePensionController {
  override implicit def authConnector: AuthConnector = MockAuthConnector

  override val customAuditConnector: CustomAuditConnector = MockCustomAuditConnector
  override val sessionCache: SessionCache = MockSessionCache
  override val metricsService: MetricsService = MockMetricsService

  override implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer
  override val applicationGlobal:ApplicationGlobalTrait = MockApplicationGlobal

  override val statePensionService: StatePensionService = MockStatePensionServiceViaStatePension
  override val nationalInsuranceService: NationalInsuranceService = MockNationalInsuranceServiceViaNationalInsurance
  override val applicationConfig: ApplicationConfig = new ApplicationConfig {
    override val assetsPrefix: String = ""
    override val reportAProblemNonJSUrl: String = ""
    override val ssoUrl: Option[String] = None
    override val betaFeedbackUnauthenticatedUrl: String = ""
    override val contactFrontendPartialBaseUrl: String = ""
    override val analyticsHost: String = ""
    override val analyticsToken: Option[String] = None
    override val betaFeedbackUrl: String = ""
    override val reportAProblemPartialUrl: String = ""
    override val showGovUkDonePage: Boolean = true
    override val govUkFinishedPageUrl: String = "govukdone"
    override val verifySignIn: String = ""
    override val verifySignInContinue: Boolean = false
    override val postSignInRedirectUrl: String = ""
    override val notAuthorisedRedirectUrl: String = ""
    override val identityVerification: Boolean = true
    override val ivUpliftUrl: String = "ivuplift"
    override val ggSignInUrl: String = "ggsignin"
    override val pertaxFrontendUrl: String = ""
    override val contactFormServiceIdentifier: String = ""
    override val breadcrumbPartialUrl: String = ""
    override val showFullNI: Boolean = false
    override val futureProofPersonalMax: Boolean = false
    override val isWelshEnabled = true
  }
}
