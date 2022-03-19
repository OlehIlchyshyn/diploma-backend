package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.common.enumeration.Currency;
import com.nulp.fetchproductdata.model.ConversionRate;
import com.nulp.fetchproductdata.parser.rates.ConversionRatesParser;
import com.nulp.fetchproductdata.repository.ConversionRateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversionService {

  private final ConversionRatesParser conversionRatesParser;
  private final ConversionRateRepository conversionRateRepository;
  private final ModelMapper modelMapper;

  public List<ConversionRate> getAllRates() {
    return mapToModel(conversionRatesParser.getConversionRates());
  }

  public ConversionRate getRate(Currency from, Currency to) {
    return conversionRateRepository.findConversionRateByFromCurrencyAndToCurrency(from, to);
  }

  public double convert(Currency from, Currency to, double amount) {
    return amount * getRate(from, to).getRate();
  }

  private List<ConversionRate> mapToModel(
      List<com.nulp.fetchproductdata.parser.rates.model.ConversionRate> conversionRates) {
    return conversionRates.stream().map(this::mapToModel).collect(Collectors.toList());
  }

  private ConversionRate mapToModel(
      com.nulp.fetchproductdata.parser.rates.model.ConversionRate conversionRate) {
    return modelMapper.map(conversionRate, ConversionRate.class);
  }
}
