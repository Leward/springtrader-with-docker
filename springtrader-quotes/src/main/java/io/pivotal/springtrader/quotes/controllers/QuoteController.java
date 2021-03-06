package io.pivotal.springtrader.quotes.controllers;


import io.pivotal.springtrader.quotes.domain.Stock;
import io.pivotal.springtrader.quotes.exceptions.SymbolNotFoundException;
import io.pivotal.springtrader.quotes.services.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Rest Controller providing the REST API for the Quote Service.
 * Provides two calls (both HTTP GET methods):
 * - /quote/{symbol} - Retrieves the current quote for a given symbol.
 * - /company/{name} - Retrieves a list of company information for companies that match the {name}.
 *
 * @author David Ferreira Pinto
 * @author cq
 */
@RestController
public class QuoteController {

    private static final Logger logger = LoggerFactory.getLogger(QuoteController.class);

    /**
     * The quoteService to delegate calls to.
     */
    @Autowired
    private QuoteService quoteService;

    /**
     * Retrives the current quote for the given symbol.
     *
     * @param symbol The symbol to retrieve the quote for.
     * @return The Stock
     * @throws SymbolNotFoundException if the symbol is not valid.
     */
    @RequestMapping(value = "/quote/{symbol}", method = RequestMethod.GET)
    public ResponseEntity<Stock> getQuote(@PathVariable("symbol") final String symbol) throws Exception {
        logger.debug("QuoteController.getQuote: retrieving quote for: " + symbol);
        Stock stock = quoteService.getQuote(symbol);
        logger.info(String.format("Retrieved symbol: %s with quote %s", symbol, stock));
        return new ResponseEntity<>(stock, getNoCacheHeaders(), HttpStatus.OK);
    }

    /**
     * Searches for companies that have a name or symbol matching the parameter.
     *
     * @param name The name or symbol to search for.
     * @return The list of companies that match the search parameter.
     */
    @RequestMapping(value = "/company/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<Stock>> getCompanies(@PathVariable("name") final String name) {
        logger.debug("QuoteController.getCompanies: retrieving companies for: " + name);
        List<Stock> companies = quoteService.companiesByNameOrSymbol(name);
        logger.info(String.format("Retrieved companies with search parameter: %s - list: {}", name), companies);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    /**
     * Generates HttpHeaders that have the no-cache set.
     *
     * @return HttpHeaders.
     */
    private HttpHeaders getNoCacheHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Cache-Control", "no-cache");
        return responseHeaders;
    }

    /**
     * Handles the response to the client if there is any exception during the processing of HTTP requests.
     *
     * @param e        The exception thrown during the processing of the request.
     * @param response The HttpResponse object.
     * @throws IOException
     */
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletResponse response) throws IOException {
        logger.error("Handle Error: " + e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), "ERROR: " + e.getMessage());
    }
}