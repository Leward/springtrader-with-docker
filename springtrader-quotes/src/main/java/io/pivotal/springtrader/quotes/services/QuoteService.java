package io.pivotal.springtrader.quotes.services;

import io.pivotal.springtrader.quotes.domain.Stock;
import io.pivotal.springtrader.quotes.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.quotes.repositories.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A service to retrieve Company and Quote information.
 *
 * @author David Ferreira Pinto
 * @author cq
 */
@Service
public class QuoteService {

	private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

	@Value("${api.url.company}")
	private String companyUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input={name}";

	@Value("${api.url.quote}")
	private String quoteUrl = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol={symbol}";

	private RestOperations restOperations = new RestTemplate();

	private StockRepository stockRepository;

	@Autowired
	public void setStockRepository(StockRepository stockRepository) {
		this.stockRepository = stockRepository;
	}

	/**
	 * Retrieves an up to date quote for the given symbol.
	 *
	 * @param symbol
	 *            The symbol to retrieve the quote for.
	 * @return The quote object or null if not found.
	 * @throws SymbolNotFoundException
	 */

	public Stock getQuote(String symbol) throws Exception {
		logger.debug("QuoteService.getQuote: retrieving quote for: " + symbol);

		symbol = symbol.toUpperCase();
		Stock stock = stockRepository.findOne(symbol);

		// what's happen if a stock has no info about its quotes?
		if (stock == null || stock.getStatus() == null) {
			stock = createStock(symbol);
			stock = stockRepository.save(stock);
		}

		return stock;

	}

	private Stock createStock(String symbol) throws SymbolNotFoundException {

		Stock returnedStock;

		try {

			Map<String, String> params = new HashMap<>();
			params.put("symbol", symbol);
			returnedStock = restOperations.getForObject(quoteUrl, Stock.class, params);
			logger.debug("QuoteService.getQuote: retrieved quote: " + returnedStock);

			Stock stock = companiesByNameOrSymbol(symbol).stream().filter(s -> s.getSymbol().equalsIgnoreCase(symbol))
					.findFirst().orElse(new Stock());

			if (stock.getSymbol() == null)
				throw new SymbolNotFoundException("Symbol not found: " + symbol);
			returnedStock.setName(stock.getName());
			returnedStock.setExchange(stock.getExchange());

		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug(e.getMessage(), e);
			else
				logger.error(e.getMessage() + " [" + e.getClass().getSimpleName() + "]" );
			throw e;
		}
		return returnedStock;
	}

	@SuppressWarnings("unchecked")
	public List<Stock> companiesByNameOrSymbol(String name) {
		logger.debug("QuoteService.companiesByNameOrSymbol: retrieving info for: " + name);
		List<Stock> stockList = new ArrayList<>();
		try {

			// only search for name.
			stockList = stockRepository.findByNameLike(name);
			if (stockList.size() > 0)
				return stockList;

			Map<String, String> params = new HashMap<>();
			params.put("name", name);
			Map<String, String>[] companies = (Map<String, String>[]) restOperations.getForObject(companyUrl,
					Map[].class, params);

			for (Map<String, String> company : companies) {
				Stock stock = new Stock();
				stock.setName(company.get("Name"));
				stock.setExchange(company.get("Exchange"));
				stock.setSymbol(company.get("Symbol"));
				stockList.add(stock);
			}

			logger.debug("QuoteService.companiesByNameOrSymbol: retrieved info: " + stockList);

		} catch (Exception e) {
			// Log and continue, return empty stockList
			if (logger.isDebugEnabled())
				logger.debug(e.getMessage(), e);
			else
				logger.error(e.getMessage() + " [" + e.getClass().getSimpleName() + "]" );
		}

		return stockList;
	}
}