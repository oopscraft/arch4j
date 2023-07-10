package org.oopscraft.arch4j.cli.database;

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

    /**
     * get schema
     * @param outFile out file path
     * @return exit value
     */
    @CommandLine.Command(name = "get-schema")
    @Transactional
    public Integer getSchema(
            @CommandLine.Option(names = {"-o","--out-file"}, description = "Output File Path") String outFile
    ) {
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

        // target type
        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.STDOUT);
        if(outFile != null) {
            schemaExport.setOutputFile(outFile);
            targetTypes = EnumSet.of(TargetType.SCRIPT);
        }
        schemaExport.execute(targetTypes, SchemaExport.Action.CREATE, metadata);

        // returns
        return 0;
    }


}
