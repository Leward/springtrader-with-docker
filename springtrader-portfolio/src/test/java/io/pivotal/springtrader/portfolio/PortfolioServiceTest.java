package io.pivotal.springtrader.portfolio;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import io.pivotal.springtrader.portfolio.domain.Account;
import io.pivotal.springtrader.portfolio.domain.Order;
import io.pivotal.springtrader.portfolio.domain.Quote;
import io.pivotal.springtrader.portfolio.repositories.OrderRepository;
import io.pivotal.springtrader.portfolio.services.PortfolioService;

public class PortfolioServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceTest.class);

	MockMvc mockMvc;

	@InjectMocks
	PortfolioService service;
	
	@Mock
	OrderRepository repo;
	
	@Mock
	RestTemplate restTemplate;

	String quotesUrl = "http://localhost:8086";

    String accountsUrl = "http://localhost:8082";
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

	    this.mockMvc = MockMvcBuilders.standaloneSetup(service).build();
        ReflectionTestUtils.setField(service,"accountsUrl",accountsUrl);
        ReflectionTestUtils.setField(service,"quotesUrl",quotesUrl);
        //ReflectionTestUtils.setField(service,"restTemplate",restTemplate);
	}

	@Test
	public void doGetPortfolio() {
 
		logger.info("RestTemplate = " + restTemplate.getClass() + " "  + restTemplate.hashCode());

		//when(quoteService.getUri()).thenReturn(uri);
		when(repo.findByAccountId(ServiceTestConfiguration.ACCOUNT_ID)).thenReturn(ServiceTestConfiguration.orders());
		when(restTemplate.getForObject(accountsUrl + "/account?userid={userid}", Account.class, ServiceTestConfiguration.order().getAccountId())).thenReturn(ServiceTestConfiguration.account());
		when(restTemplate.getForObject(quotesUrl + "/quote/{symbol}", Quote.class, ServiceTestConfiguration.quote().getSymbol())).thenReturn(ServiceTestConfiguration.quote());
		service.getPortfolio(ServiceTestConfiguration.ACCOUNT_ID);
	}
	@Test
	public void doSaveOrder() {
		Order returnOrder = ServiceTestConfiguration.order();
		returnOrder.setOrderId(1);
		double amount = ServiceTestConfiguration.order().getQuantity()*ServiceTestConfiguration.order().getPrice().doubleValue()+ServiceTestConfiguration.order().getOrderFee().doubleValue();
		ResponseEntity<Double> response = new ResponseEntity<Double>(100d, HttpStatus.OK);
		
		
		//when(accountService.getUri()).thenReturn(uri);
		when(restTemplate.getForEntity(accountsUrl + "/accounts/{userid}/decreaseBalance/{amount}", Double.class, ServiceTestConfiguration.order().getAccountId(), amount )).thenReturn(response);
		when(repo.save(ServiceTestConfiguration.order())).thenReturn(returnOrder);
		Order order = service.addOrder(ServiceTestConfiguration.order());
		assertEquals(order, returnOrder);
	}
	
	@Test
	public void doSaveOrderNullOrderFee() {
		Order returnOrder = ServiceTestConfiguration.order();
		returnOrder.setOrderId(1);
		double amount = returnOrder.getQuantity()*returnOrder.getPrice().doubleValue()+returnOrder.getOrderFee().doubleValue();
		ResponseEntity<Double> response = new ResponseEntity<Double>(100d, HttpStatus.OK);
		
		
		//when(accountService.getUri()).thenReturn(uri);
		when(restTemplate.getForEntity(Matchers.any(), Matchers.eq(Double.class), Matchers.any(), Matchers.any())).thenReturn(response);
		when(repo.save(Matchers.isA(Order.class))).thenReturn(returnOrder);
		Order requestOrder = ServiceTestConfiguration.order();
		requestOrder.setOrderFee(null);
		Order order = service.addOrder(requestOrder);
		assertEquals(order.getOrderFee(), ServiceTestConfiguration.order().getOrderFee());
	}
	@Test
	public void doSaveOrderSellOrder() {
		Order returnOrder = ServiceTestConfiguration.sellOrder();
		returnOrder.setOrderId(1);
		double amount = ServiceTestConfiguration.sellOrder().getQuantity()*ServiceTestConfiguration.sellOrder().getPrice().doubleValue()-ServiceTestConfiguration.sellOrder().getOrderFee().doubleValue();
		ResponseEntity<Double> response = new ResponseEntity<Double>(100d, HttpStatus.OK);
		
		
		//when(accountService.getUri()).thenReturn(uri);
		when(restTemplate.getForEntity(accountsUrl + "/accounts/{userid}/increaseBalance/{amount}", Double.class, ServiceTestConfiguration.sellOrder().getAccountId(), amount )).thenReturn(response);
		when(repo.save(ServiceTestConfiguration.sellOrder())).thenReturn(returnOrder);
		Order order = service.addOrder(ServiceTestConfiguration.sellOrder());
		assertEquals(order, returnOrder);
	}

}
