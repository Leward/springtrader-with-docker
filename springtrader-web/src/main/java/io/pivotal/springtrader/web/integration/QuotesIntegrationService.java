package io.pivotal.springtrader.web.integration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.pivotal.springtrader.web.domain.CompanyInfo;
import io.pivotal.springtrader.web.domain.MarketSummary;
import io.pivotal.springtrader.web.domain.Quote;

@Service
@EnableScheduling
public class QuotesIntegrationService {

	private static final Logger logger = LoggerFactory.getLogger(QuotesIntegrationService.class);
	private final static Integer QUOTES_NUMBER = 3;

	// 10 minutes in milliseconds
	private final static long REFRESH_PERIOD = 600000l;

	@Value("${quotes.url}")
	private String quotesUrl;

	private RestTemplate restTemplate = new RestTemplate();

	private static List<String> symbolsIT = Arrays.asList("EMC", "ORCL", "IBM", "INTC", "AMD", "HPQ", "CSCO", "AAPL");
	private static List<String> symbolsFS = Arrays.asList("JPM", "MS", "BAC", "GS", "WFC", "BK");

	private MarketSummary summary = new MarketSummary();

	public MarketSummary getMarketSummary() {
		logger.debug("Retrieving Market Summary");
		if(summary == null || summary.getTopGainers() == null || summary.getTopLosers() == null) {
			retrieveMarketSummary();
		}
		return summary;
	}

	public Quote getQuote(String symbol) {
		logger.debug("Fetching quote: " + symbol);
		Quote quote = restTemplate.getForObject(quotesUrl + "/quote/{symbol}", Quote.class, symbol);
		return quote;
	}

	public List<CompanyInfo> getCompanies(String name) {
		logger.debug("Fetching companies with name or symbol matching: " + name);
		CompanyInfo[] infos = restTemplate.getForObject(quotesUrl + "/company/{name}", CompanyInfo[].class, name);
		return Arrays.asList(infos);
	}

	// TODO: prime location for a redis/gemfire caching service!
	@Scheduled(fixedRate = REFRESH_PERIOD)
	protected void retrieveMarketSummary() {
		logger.debug("Scheduled retrieval of Market Summary");
		List<Quote> quotesIT = pickRandomThree(symbolsIT).parallelStream().map(symbol -> getQuote(symbol))
				.collect(Collectors.toList());
		List<Quote> quotesFS = pickRandomThree(symbolsFS).parallelStream().map(symbol -> getQuote(symbol))
				.collect(Collectors.toList());
		summary.setTopGainers(quotesIT);
		summary.setTopLosers(quotesFS);
	}

	private List<String> pickRandomThree(List<String> symbols) {
		// Shuffle randomly and pick the first three
		Collections.shuffle(symbols);
		return symbols.subList(0, QUOTES_NUMBER);

	}
}
