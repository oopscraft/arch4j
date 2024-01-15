# Spring Batch Overview

```java

import java.beans.BeanProperty;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileDsvToFileFldBatch extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    @Bean
    Job fileDsvToFileFldJob() {
        return getJobBuilder("fileDsvToFileFldJob")
                .start(firstSstep())
                .next(SecondStep())
                .build();
    }
    
    @Bean
    @JobScope
    Step firstStep() {
        return getStepBuilder("firstStep")
                .<___,___>chunck(10) 
                .reader(___Reader(null))
                .processor(___Processor())
                .writer(___Writer())
                .build();
    }
    
    @Bean
    @StepScope
    ___Reader ___Reader(@Value("#{jobParameters[param]}") String param) {
        // return item reader
    }
    
    @Bean
    @StepScope
    ItemProcessor<___,___> ___Processor() {
        // return item processor 
    }
    
    @Bean
    @StepScope
    ___Writer<___> ___Writer() {
        // return item writer
    }
   
    @Bean
    @JobScope
    Step secondStep() {
        // return step        
    }
    
}
```