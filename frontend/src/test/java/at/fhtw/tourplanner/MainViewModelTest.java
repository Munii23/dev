package at.fhtw.tourplanner;

import javafx.scene.Parent;
import javafx.scene.control.TableCell;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ButtonMatchers;
import org.testfx.service.query.impl.NodeQueryImpl;
import org.testfx.util.NodeQueryUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)

class MainViewModelTest {

    private Parent root = null;
    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage primaryStage) throws Exception{
        root = Main.showStage(primaryStage);
    }


}