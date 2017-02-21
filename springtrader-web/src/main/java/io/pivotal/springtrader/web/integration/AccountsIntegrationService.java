package io.pivotal.springtrader.web.integration;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.pivotal.springtrader.web.domain.Account;
import io.pivotal.springtrader.web.domain.AuthenticationRequest;

@Service
public class AccountsIntegrationService {

	private static final Logger logger = LoggerFactory.getLogger(AccountsIntegrationService.class);

	@Value("${accounts.url}")
	private String accountsUrl;

	private RestTemplate restTemplate  = new RestTemplate();

	@PostConstruct
	public void checkConfiguration() {
		if (accountsUrl.indexOf("xxxx-yyyy") != -1)
			throw new IllegalStateException("Set the URLS in the cloud profile section of 'application.yml'");
	}

	public void createAccount(Account account) {
		logger.debug("Saving account with userId: " + account.getUserid());
		String status = restTemplate.postForObject(accountsUrl + "/account/", account, String.class);
		logger.info("Status from registering account for " + account.getUserid() + " is " + status);
	}

	public Map<String, Object> login(AuthenticationRequest request) {
		logger.debug("logging in with userId:" + request.getUsername());
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) restTemplate.postForObject(accountsUrl + "/login/", request,
				Map.class);
		return result;
	}

	// TODO: change to /account/{user}
	public Account getAccount(String user) {
		logger.debug("Looking for account with userId: " + user);

		Account account = restTemplate.getForObject(accountsUrl + "/account/?userid={user}", Account.class, user);
		logger.debug("Got Account: " + account);
		return account;
	}

	public void logout(String user) {
		logger.debug("logging out account with userId: " + user);

		ResponseEntity<?> response = restTemplate.getForEntity(accountsUrl + "/logout/{user}", String.class, user);
		logger.debug("Logout response: " + response.getStatusCode());
	}

}
