/* @author Zsolt Ollé */
package legyenonismilliomos;

import javax.swing.SwingWorker;

public class MyWorker extends SwingWorker<Integer, String>{

    @Override
    protected Integer doInBackground() throws Exception {
        for (int i = 3; i >= 0; i--) {
//            setProgress(i);
            publish(i+"");
            Thread.sleep(1000);
        }
        return 10;
    }
    
}
