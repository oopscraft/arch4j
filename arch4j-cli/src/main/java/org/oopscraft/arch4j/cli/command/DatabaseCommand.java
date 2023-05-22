package org.oopscraft.arch4j.cli.command;

import lombok.RequiredArgsConstructor;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.hibernate.tool.schema.internal.HibernateSchemaManagementTool;
import org.hibernate.tool.schema.spi.SchemaManagementTool;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import picocli.CommandLine;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.List;

@CommandLine.Command(name = "database")
@Component
@RequiredArgsConstructor
public class DatabaseCommand {

    private final EntityManagerFactory entityManagerFactory;

    @CommandLine.Command(name = "generate-schema")
    @Transactional
    public Integer generateSchema(@CommandLine.Parameters(index = "0") String outputFile) {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        ServiceRegistry serviceRegistry = sessionFactory.getServiceRegistry().getParentServiceRegistry();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Metamodel metamodel = entityManagerFactory.getMetamodel();
        for (EntityType<?> entityType : metamodel.getEntities()) {
            metadataSources.addAnnotatedClass(entityType.getJavaType());
        }
        Metadata metadata = metadataSources.buildMetadata();
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.setOverrideOutputFileContent();
        schemaExport.setOutputFile(outputFile);
        schemaExport.execute(EnumSet.of(TargetType.SCRIPT), SchemaExport.Action.CREATE, metadata);
        return 0;
    }



}
