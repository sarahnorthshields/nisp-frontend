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

package uk.gov.hmrc.nisp.views

import org.scalatest._
import org.scalatest.mock.MockitoSugar

import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.SessionCache
import uk.gov.hmrc.nisp.config.ApplicationConfig
import uk.gov.hmrc.nisp.config.wiring.NispFormPartialRetriever
import uk.gov.hmrc.nisp.controllers._
import uk.gov.hmrc.nisp.controllers.connectors.CustomAuditConnector
import uk.gov.hmrc.nisp.helpers._
import uk.gov.hmrc.nisp.models.forms.QuestionnaireForm
import uk.gov.hmrc.nisp.services.{CitizenDetailsService, MetricsService, NationalInsuranceService, StatePensionService}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.partials.CachedStaticHtmlPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import uk.gov.hmrc.nisp.utils.MockTemplateRenderer


class QuestionnairFormViewSpec extends HtmlSpec  with MockitoSugar with BeforeAndAfter  {

  implicit val cachedStaticHtmlPartialRetriever = MockCachedStaticHtmlPartialRetriever
  implicit val formPartialRetriever: uk.gov.hmrc.play.partials.FormPartialRetriever = NispFormPartialRetriever
  implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer

  val controller = new StatePensionController {

    override val statePensionService: StatePensionService = mock[StatePensionService]
    override val nationalInsuranceService: NationalInsuranceService = mock[NationalInsuranceService]

    override lazy val customAuditConnector: CustomAuditConnector = ???
    override lazy val applicationConfig: ApplicationConfig = ???
    override val citizenDetailsService: CitizenDetailsService = MockCitizenDetailsService

    override val authConnector: AuthConnector = MockAuthConnector

    override val sessionCache: SessionCache = MockSessionCache
    override val metricsService: MetricsService = MockMetricsService
    override implicit val cachedStaticHtmlPartialRetriever: CachedStaticHtmlPartialRetriever = MockCachedStaticHtmlPartialRetriever
  }

  "Questionnaire form" should {

    lazy val sResult = html.questionnaire(QuestionnaireForm.form)
    lazy val htmlAccountDoc = asDocument(contentAsString(sResult))

    "render page with heading  you have signed out of you account " in {

      assertEqualsMessage(htmlAccountDoc, "article.content__body>h1", "nisp.questionnaire.header")
    }
    "render page with text  'to use the service again you’ll need to sign in.' " in {

      assertContainsDynamicMessage(htmlAccountDoc, "article.content__body>p:nth-child(2)", "nisp.useagain", "/check-your-state-pension", null, null)
    }
    "render page with text  'Give us feedback to help us improve this service. It will take no more than 2 minutes.' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>p:nth-child(3)", "nisp.questionnaire.please")
    }


    /*Main form starts here*/

    "render page with text  'How easy was it to use Check your State Pension?' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>legend:nth-child(1)", "nisp.questionnaire.easytouse.question")
    }
    "render page with text  'When you used this service, did you:' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(4)>legend:nth-child(1)", "nisp.questionnaire.useitbyyourself.question")
    }
    "render page with text  'How likely would you be to use this service again?' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>legend:nth-child(1)", "nisp.questionnaire.likelytouse.question")
    }
    "render page with text  'overall, how did you feel about the service you received today?' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>legend:nth-child(1)", "nisp.questionnaire.satisfied.question")
    }
    "render page with text  'After using Check your State Pension, do you feel you have:' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(7)>legend:nth-child(1)", "nisp.questionnaire.understanding.question")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next:' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>legend:nth-child(1)", "nisp.questionnaire.whatwillyoudonext.question")
    }
    "render page with text  'How could we improve the service?' " in {
      assertElemetsOwnMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(10)>label", "nisp.questionnaire.improve.question")
    }
    "render page with text  'Please do not include any personal or financial information, for example your National Insurance or credit card number.' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(10)>label>small", "nisp.textentry.warning")
    }
    "render page with text  'Would you like to take part in any future research to help us improve the service?' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(11)>legend:nth-child(1)", "nisp.questionnaire.research.question")
    }
    "render page with text  'Please leave your email address so we can cotact you' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>label:nth-child(12)>span", "nisp.questionnaire.email")
    }

    "render page with text  'How easy was it to use Check your State Pension - very easy' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(2)>label:nth-child(2)",
        "nisp.questionnaire.easytouse.0", "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(3)")
    }

    "render page with text  'How easy was it to use Check your State Pension - easy' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(3)>label:nth-child(2)",
        "nisp.questionnaire.easytouse.1", "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(4)")
    }
    "render page with text  'How easy was it to use Check your State Pension - diff' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(4)>label:nth-child(2)",
        "nisp.questionnaire.easytouse.2", "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(5)")
    }
    "render page with text  'How easy was it to use Check your State Pension - very diff' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(3)>div:nth-child(5)>label:nth-child(2)",
        "nisp.questionnaire.easytouse.3", "article.content__body>form:nth-child(4)>fieldset:nth-child(4)")
    }

    "render page with text  'When you used this service, did you: - use it by yourself' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(4)>div:nth-child(2)>label:nth-child(2)",
        "nisp.questionnaire.useitbyyourself.0", "article.content__body>form:nth-child(4)>fieldset:nth-child(4)>div:nth-child(3)")
    }
    "render page with text  'When you used this service, did you: - use it with someone' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(4)>div:nth-child(3)>label:nth-child(2)",
        "nisp.questionnaire.useitbyyourself.1", "article.content__body>form:nth-child(4)>fieldset:nth-child(4)>div:nth-child(4)")
    }
    "render page with text  'When you used this service, did you: - have someone one to use it for you' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(4)>div:nth-child(4)>label:nth-child(2)",
        "nisp.questionnaire.useitbyyourself.2", "article.content__body>form:nth-child(4)>fieldset:nth-child(5)")
    }

    "render page with text  'How likely would you be to use this service again? - very likely' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(2)>label:nth-child(2)",
        "nisp.questionnaire.likely.0", "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(3)")
    }
    "render page with text  'How likely would you be to use this service again? - likely' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(3)>label:nth-child(2)",
        "nisp.questionnaire.likely.1", "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(4)")
    }
    "render page with text  'How likely would you be to use this service again? - unlikely' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(4)>label:nth-child(2)",
        "nisp.questionnaire.likely.2", "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(5)")
    }
    "render page with text  'How likely would you be to use this service again? - very unlikely' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(5)>div:nth-child(5)>label:nth-child(2)",
        "nisp.questionnaire.likely.3", "article.content__body>form:nth-child(4)>fieldset:nth-child(6)")
    }

    "render page with text  'Overall, how did you feel about the service you received today? - very satisfied' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(2)>label:nth-child(2)",
        "nisp.questionnaire.satisfied.0", "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(3)")
    }
    "render page with text  'Overall, how did you feel about the service you received today? - satisfied' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(3)>label:nth-child(2)",
        "nisp.questionnaire.satisfied.1", "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(4)")
    }
    "render page with text  'Overall, how did you feel about the service you received today? - neither satisfied' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(4)>label:nth-child(2)",
        "nisp.questionnaire.satisfied.2", "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(5)")
    }
    "render page with text  'Overall, how did you feel about the service you received today? - unsatisfied' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(5)>label:nth-child(2)",
        "nisp.questionnaire.satisfied.3", "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(6)")
    }
    "render page with text  'Overall, how did you feel about the service you received today? - very unsatisfied' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(6)>div:nth-child(6)>label:nth-child(2)",
        "nisp.questionnaire.satisfied.4", "article.content__body>form:nth-child(4)>fieldset:nth-child(7)")
    }

    "render page with text  'After using Check your State Pension, do you feel you have: - better understanding' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(7)>div:nth-child(2)>label:nth-child(2)",
        "nisp.questionnaire.understanding.0", "article.content__body>form:nth-child(4)>fieldset:nth-child(7)>div:nth-child(3)")
    }
    "render page with text  'After using Check your State Pension, do you feel you have: - less understanding' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(7)>div:nth-child(3)>label:nth-child(2)",
        "nisp.questionnaire.understanding.1", "article.content__body>form:nth-child(4)>fieldset:nth-child(7)>div:nth-child(4)")
    }
    "render page with text  'After using Check your State Pension, do you feel you have: - no understanding' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(7)>div:nth-child(4)>label:nth-child(2)",
        "nisp.questionnaire.understanding.2", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)")
    }

    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - speak to financial advisor' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(2)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.0", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(3)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Speak to DWP/HMRC' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(3)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.1", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(4)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Speak to your employer' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(4)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.2", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(5)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Speak to friends and family' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(5)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.3", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(6)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Look into my other pensions/savings/other assets' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(6)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.4", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(7)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Get more pensions information online' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(7)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.5", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(8)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Pay gaps in my National Insurance record' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(8)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.6", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(9)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Nothing' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(9)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.7", "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(10)")
    }
    "render page with text  'After using Check your State Pension, which of the following are you most likely to do next: - Other' " in {
      assertContainsMessageBetweenTags(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(8)>div:nth-child(10)>label:nth-child(2)",
        "nisp.questionnaire.whatwillyoudonext.8", "article.content__body>form:nth-child(4)>label:nth-child(9)")
    }
    "render page with text  'How could we improve the service? limit characters' " in {
      assertEqualsMessage(htmlAccountDoc, "article.content__body>form:nth-child(4)>fieldset:nth-child(10)>span", "nisp.textentry.charlimit")
    }

  }

}
