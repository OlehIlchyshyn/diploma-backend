package com.nulp.fetchproductdata.service;

import com.nulp.fetchproductdata.parser.rozetka.products.specs.RozetkaTechCharacteristicsParser;
import com.nulp.fetchproductdata.parser.rozetka.products.specs.model.TechCharacteristics;
import com.nulp.fetchproductdata.parser.rozetka.products.specs.model.TechCharacteristicsGroup;
import com.nulp.fetchproductdata.parser.rozetka.products.specs.model.TechDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechCharacteristicsService {

    private final RozetkaTechCharacteristicsParser techCharacteristicsParser;

    public Map<String, Map<String, String>> getTechCharacteristicsByProductId(Integer id) {
        TechCharacteristics techCharacteristics = techCharacteristicsParser.getTechCharacteristicsByProductId(id);
        return techCharacteristics.getTechCharacteristicsGroupList()
                .stream()
                .collect(Collectors.toMap(
                        TechCharacteristicsGroup::getGroupTitle,
                        (value) -> value.getSpecs().stream()
                                .collect(Collectors.toMap(TechDetail::getTitle,
                                        (techDetail) -> techDetail.getValues().get(0).getTitle(),
                                        (firstTitle, secondTitle) -> {
                                            log.warn("Duplicate tech detail key: " + firstTitle);
                                            return firstTitle;
                                        }))));
    }
}
