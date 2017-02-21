package io.pivotal.springtrader.web.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.pivotal.springtrader.web.domain.Order;
import io.pivotal.springtrader.web.domain.Portfolio;
import io.pivotal.springtrader.web.exceptions.OrderNotSavedException;

/**
 * Created by cq on 3/11/15.
 */
@Service
public class PortfolioIntegrationService {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioIntegrationService.class);

	@Value("${portfolios.url}")
	private String portfolioUrl;

	private RestTemplate restTemplate = new RestTemplate();

	public Order sendOrder(Order order) throws OrderNotSavedException {
		logger.debug("send order: " + order);

		// check result of http request to ensure its ok.

		ResponseEntity<Order> result = restTemplate.postForEntity(portfolioUrl + "/portfolio/{accountId}", order,
				Order.class, order.getAccountId());
		if (result.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			throw new OrderNotSavedException("Could not save the order");
		}

		logger.debug("Order saved:: " + result.getBody());
		return result.getBody();
	}

	public Portfolio getPortfolio(String accountId) {
		logger.info("GET portfolio for " + accountId);
		
		Portfolio folio = restTemplate.getForObject(portfolioUrl + "/portfolio/{accountid}", Portfolio.class,
				accountId);
		logger.debug("Portfolio received: " + folio);
		return folio;
	}
}
