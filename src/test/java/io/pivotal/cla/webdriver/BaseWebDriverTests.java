/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.cla.webdriver;

import static org.mockito.Mockito.reset;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.pivotal.cla.data.repository.AccessTokenRepository;
import io.pivotal.cla.data.repository.ContributorLicenseAgreementRepository;
import io.pivotal.cla.data.repository.UserRepository;
import io.pivotal.cla.service.GitHubService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebDriverContext
public abstract class BaseWebDriverTests {

	@Autowired
	WebApplicationContext wac;
	protected GitHubService mockGithub;
	protected ContributorLicenseAgreementRepository mockClaRepository;
	protected AccessTokenRepository mockTokenRepo;
	protected UserRepository userRepo;

	protected WebDriver driver;

	protected MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.apply(springSecurity())
				// .alwaysDo(print())
				.build();

		driver = MockMvcHtmlUnitDriverBuilder
				.mockMvcSetup(mockMvc)
				.useMockMvcForHosts("github.com")
				.build();
	}

	@Autowired
	public void setMockGithub(GitHubService mockGithub) {
		reset(mockGithub);
		this.mockGithub = mockGithub;
	}

	@Autowired
	public void setMockClaRepository(ContributorLicenseAgreementRepository mockClaRepository) {
		reset(mockClaRepository);
		this.mockClaRepository = mockClaRepository;
	}

	@Autowired
	public void setMockTokenRepo(AccessTokenRepository mockTokenRepo) {
		reset(mockTokenRepo);
		this.mockTokenRepo = mockTokenRepo;
	}

	protected WebDriver getDriver() {
		return driver;
	}

	protected static String urlDecode(String value) throws UnsupportedEncodingException {
		return URLDecoder.decode(value, "UTF-8");
	}
}