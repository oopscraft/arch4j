package org.chomookun.arch4j.shell.command;

import lombok.RequiredArgsConstructor;
//import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

@ShellComponent
@Component
@RequiredArgsConstructor
public class DatabaseCommand {

    private final EntityManagerFactory entityManagerFactory;

    @ShellMethod(key = "get-schema", value = "get schema ddl")
    public Integer getSchema(String outFile) {
//        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
//        ServiceRegistry serviceRegistry = sessionFactory.getServiceRegistry().getParentServiceRegistry();
//        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
//        Metamodel metamodel = entityManagerFactory.getMetamodel();
//        for (EntityType<?> entityType : metamodel.getEntities()) {
//            metadataSources.addAnnotatedClass(entityType.getJavaType());
//        }
//        Metadata metadata = metadataSources.buildMetadata();
//        SchemaExport schemaExport = new SchemaExport();
//        schemaExport.setFormat(true);
//        schemaExport.setDelimiter(";");
//        schemaExport.setOverrideOutputFileContent();
//
//        // target type
//        EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.STDOUT);
//        if(outFile != null) {
//            schemaExport.setOutputFile(outFile);
//            targetTypes = EnumSet.of(TargetType.SCRIPT);
//        }
//        schemaExport.execute(targetTypes, SchemaExport.Action.CREATE, metadata);

        // returns
        return 0;
    }


}
