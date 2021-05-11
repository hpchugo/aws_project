package com.github.hpchugo.aws_project02.repository;

import com.github.hpchugo.aws_project02.model.ProductEventKey;
import com.github.hpchugo.aws_project02.model.ProductEventLog;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableScan
@Repository
public interface ProductEventLogRepository extends CrudRepository<ProductEventLog, ProductEventKey> {
    List<ProductEventLog> findAllByPk(String code);
    List<ProductEventLog> findAllByPkAndSkStartsWith(String code, String eventCode);
}
