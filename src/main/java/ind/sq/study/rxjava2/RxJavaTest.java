package ind.sq.study.rxjava2;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJavaTest {
    private static final Logger logger = LoggerFactory.getLogger(RxJavaTest.class);

    public void testRxJava() {
        var observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                logger.info("Observable emit 1");
                e.onNext(1);
                Thread.sleep(1000);
                logger.info("Observable emit 2");
                e.onNext(2);
                Thread.sleep(1000);
                logger.info("Observable emit 3");
                e.onNext(3);
                Thread.sleep(1000);
                logger.info("Observable emit 4");
                e.onNext(4);
            }

        }).subscribeOn(Schedulers.newThread()).doOnNext(result -> logger.info("In doOnNext: {}", result));

        observable.observeOn(Schedulers.io()).subscribe(new Observer<Integer>() {
            private int i;
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                this.disposable = d;
            }

            @Override
            public void onNext(Integer t) {
                logger.info("Receive {}", t);
                i++;
                if (i == 4) {
                    disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                logger.info("on Error {}", e.getMessage());
            }

            @Override
            public void onComplete() {
                logger.info("on Complete");
            }

        });

        observable.subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer t) throws Exception {
                logger.info("accepted {}", t);
            }

        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private void testFlowable() {
        var disposable = Flowable.interval(3, TimeUnit.SECONDS)
                .subscribe(value -> logger.info("The value is {}", value));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable.dispose();
        logger.info("Interval disposed");

    }

    private void testLast() {
        Observable.just(1, 2, 3, 4).last(2).subscribe(value -> logger.info("Last value is {}", value));
        Observable.empty().last(2).subscribe(value -> logger.info("Last value for empty Observable is the default value {}", value));
    }

    public static void main(String[] args) throws Exception {
        var rjt = new RxJavaTest();
        rjt.testLast();

        logger.info("Main thread exit");
    }
}
