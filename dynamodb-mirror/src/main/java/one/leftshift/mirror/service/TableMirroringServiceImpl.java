package one.leftshift.mirror.service;

import one.leftshift.common.concurrent.AbstractThreadPoolService;
import one.leftshift.common.dynamodb.AmazonDynamoDBFactory;
import one.leftshift.common.dynamodb.repository.SynchronousDynamoDBRepository;
import one.leftshift.common.util.Tuple;
import one.leftshift.mirror.MirrorRequest;
import one.leftshift.mirror.service.task.DefaultMirroringTask;
import one.leftshift.mirror.service.task.MirroringContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author benjamin.krenn@leftshift.one - 10/17/18.
 * @since 1.0.0
 */
public class TableMirroringServiceImpl extends AbstractThreadPoolService implements TableMirroringService {

    public TableMirroringServiceImpl(ExecutorService executorService) {
        super(executorService);
    }

    public TableMirroringServiceImpl() {
        super(Executors.newFixedThreadPool(5));
    }

    @Override
    public void mirror(MirrorRequest mirrorRequest) {
        mirrorRequest.tableNames()
                .forEach(table -> this.executorService.submit(
                        new DefaultMirroringTask(MirroringContext.from(Tuple.of(mirrorRequest.from(), mirrorRequest.to()), table),
                                new SynchronousDynamoDBRepository(AmazonDynamoDBFactory.synchronous(mirrorRequest.from()), table),
                                new SynchronousDynamoDBRepository(AmazonDynamoDBFactory.synchronous(mirrorRequest.to()), table)
                )));
        this.gracefullyShutdownThreadPool();
    }
}
