package com.reeco.core.dmp.openapi;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.reeco.core.dmp.common.OpenApiAdapter;
import com.reeco.core.dmp.openapi.cassandra.entity.NumericalTsByOrg;
import com.reeco.core.dmp.openapi.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.shares.api.dmp.view.ChartDto;
import com.reeco.shares.api.dmp.view.ChartResolution;
import com.reeco.shares.api.dmp.view.DataPointDto;
import com.reeco.shares.api.dmp.view.ParameterDataDto;
import com.reeco.shares.api.dmp.view.ParameterDto;
import com.reeco.shares.api.dmp.view.ViewService;
import com.reeco.shares.util.exceptions.InvalidInputException;
import com.reeco.shares.util.http.ServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@OpenApiAdapter
@RestController
@RequiredArgsConstructor
public class ViewQueryController implements ViewService {

    private static final Logger LOG = LoggerFactory.getLogger(ViewQueryController.class);

    private final ServiceUtil serviceUtil;

    @Autowired
    private NumericalTsByOrgRepository  numericalTsByOrgRepository;

    // private final ViewQuery viewQuery;

    // private final ViewMapper viewMapper;

    // @Override
    // public Mono<ViewDto> getView(int viewId) {
    // LOG.debug("/view return the found view for viewId={}", viewId);

    // if (viewId < 1)
    // throw new InvalidInputException("Invalid viewId: " + viewId);

    // return viewQuery.getView(viewId)
    // .switchIfEmpty(error(new NotFoundException("No view found for viewId: " +
    // viewId))).log()
    // .map(e -> viewMapper.entityToApi(e)).map(e -> {
    // e.setServiceAddress(serviceUtil.getServiceAddress());
    // return e;
    // });

    // }

    // @Override
    // public Mono<ViewDto> createView(ViewDto body) {

    // if (body.getViewId() < 1)
    // throw new InvalidInputException("Invalid accountId: " + body.getViewId());

    // try {

    // LOG.debug("createView: creates a new account entity for userId: {}",
    // body.getUserId());
    // View viewEntity = viewMapper.apiToEntity(body);
    // Mono<ViewDto> newEntity = viewQuery.saveView(viewEntity).log()
    // .onErrorMap(DuplicateKeyException.class,
    // ex -> new InvalidInputException("Duplicate key, View Id: " +
    // body.getViewId()))
    // .map(e -> viewMapper.entityToApi(e));

    // return newEntity;

    // } catch (RuntimeException re) {
    // LOG.warn("createView failed: {}", re.toString());
    // throw re;
    // }
    // }

    // @Override
    // public Mono<Void> deleteView(int viewId) {
    // if (viewId < 1)
    // throw new InvalidInputException("Invalid viewId: " + viewId);

    // LOG.debug("deleteView: tries to delete an entity with viewId: {}", viewId);
    // return viewQuery.deleteView(viewId);
    // }

    @Override
    public Mono<ChartDto> viewChartSeries(ChartDto chartDto) {
        System.out.println(chartDto);
        // Timestamp startTime = new Timestamp(chartDto.getStartTime().getTime());
        if (chartDto.getStartTime().isAfter(chartDto.getEndTime())) {
            throw new InvalidInputException("Invalid Time Range");
        }
        ChartDto chartDto1 = new ChartDto();

        // chartDto1.setStationId(chartDto.getStationId());
        // chartDto1.setParameterDtos(chartDto.getParameterDtos());

        Flux<NumericalTsByOrg> lByOrgs =  numericalTsByOrgRepository.findByPartitionKeyOrganizationIdAndPartitionKeyParamId(1L, chartDto.getParameterDtos().get(0).getParameterId());
        System.out.println(lByOrgs);
        List<ParameterDataDto> indicatorDataDtos = new ArrayList<>();
        for (ParameterDto indicatorDto : chartDto.getParameterDtos()) {

            ParameterDataDto indicatorDataDto = new ParameterDataDto();
            indicatorDto.setIndicatorType("NUMBER");
            indicatorDataDto.setParameterDto(indicatorDto);
            List<DataPointDto> dataPointDtos = new ArrayList<>();
            if (chartDto.getStartTime().equals(chartDto.getEndTime())) {
                chartDto1.setStartTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                chartDto1.setEndTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                // Query Data point
                for (int j = 0; j < 2; j++) {
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
                            .minus(Duration.of(j * 10, ChronoUnit.MINUTES)));
                    Random generator = new Random();
                    dataPointDto.setValue(String.valueOf(generator.nextInt((30 - 10) + 1) + 10));
                    // dataPointDto.setLat(generator.nextDouble() * 360.0);
                    // dataPointDto.setLon(generator.nextDouble() * 360.0);
                    dataPointDtos.add(dataPointDto);
                }
            } else {
                // Data series
                chartDto1.setStartTime(chartDto.getStartTime());
                chartDto1.setEndTime(chartDto.getEndTime());
                Timestamp endTimestamp = Timestamp.valueOf(chartDto.getStartTime());
                ChartResolution chartResolution = ChartResolution.valueOf(chartDto.getResolution());
                while (endTimestamp.before(Timestamp.valueOf(chartDto.getEndTime()))) {
                    DataPointDto dataPointDto = new DataPointDto();
                    dataPointDto.setEventTime(endTimestamp.toLocalDateTime());
                    endTimestamp = new Timestamp(
                            endTimestamp.getTime() + (int) (chartResolution.getValueFromEnum() * 3600000));
                    Random generator = new Random();
                    dataPointDto.setValue(String.valueOf(generator.nextInt((30 - 10) + 1) + 10));
                    // switch (chartResolution) {
                    // case DEFAULT:
                    // dataPointDto.setEventTime(endTimestamp.toLocalDateTime());
                    // endTimestamp = new Timestamp(endTimestamp.getTime() + 600000);
                    // break;
                    // case HOUR_4:
                    // dataPointDto.setEventTime(endTimestamp.toLocalDateTime());
                    // endTimestamp = new Timestamp(endTimestamp.getTime() + 14400000);

                    // break;
                    // case DAY_1:
                    // dataPointDto.setEventTime(endTimestamp.toLocalDateTime());
                    // endTimestamp = new Timestamp(endTimestamp.getTime() + 86400000);
                    // break;
                    // default:
                    // throw new InvalidInputException("Invalid Resolution");
                    // }

                    dataPointDtos.add(dataPointDto);

                }
                // for (int j = 0; j < 50; j++) {
                // DataPointDto dataPointDto = new DataPointDto();
                // dataPointDto.setEventTime(new
                // Timestamp((Timestamp.valueOf(chartDto.getStartTime()).getTime()
                // + (Timestamp.valueOf(chartDto.getEndTime()).getTime()
                // - Timestamp.valueOf(chartDto.getStartTime()).getTime()) * j / 50))
                // .toLocalDateTime());
                // Random generator = new Random();
                // dataPointDto.setValue(String.valueOf(generator.nextInt((30 - 10) + 1) + 10));
                // // dataPointDto.setLat(generator.nextDouble() * 360.0);
                // // dataPointDto.setLon(generator.nextDouble() * 360.0);
                // dataPointDtos.add(dataPointDto);
                // }
            }
            indicatorDataDto.setDataPointDtos(dataPointDtos);
            indicatorDataDtos.add(indicatorDataDto);

        }
        chartDto1.setParameterDatas(indicatorDataDtos);

        // Call data from chartQuery
        Mono<ChartDto> chartDtoMono = Mono.just(chartDto1);
        return chartDtoMono;
    }

}