/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.javaFx.utils;

import static javafx.beans.binding.Bindings.add;
import static javafx.beans.binding.Bindings.divide;
import static javafx.beans.binding.Bindings.multiply;
import static javafx.beans.binding.Bindings.subtract;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

/**
 *
 * @author Noam
 */
public class SnakesAndLaddersDrawingUtil {

    private static final String RESOURCES_DIR = "/resources/";
    private static final String IMAGES_DIR = RESOURCES_DIR + "images/";
    private static final String LADDER_PATTERN_FILENAME = IMAGES_DIR + "ladderpattern.png";
    private static final String SNAKE_PATTERN_FILENAME = IMAGES_DIR + "snake.png";

    private final int size;
    private final GridPane cellsPane;
    private final Pane snakesAndLaddersPane;

    public SnakesAndLaddersDrawingUtil(int size, GridPane cellsPane, AnchorPane boardView) {
        this.size = size;
        snakesAndLaddersPane = createSnakesAndLaddersPane();
        if (cellsPane != null) {
            this.cellsPane = cellsPane;
        } else {
            this.cellsPane = null;
        }
        boardView.getChildren().addAll(snakesAndLaddersPane);
    }

    private Pane createSnakesAndLaddersPane() {
        Pane pane = new Pane();
        pane.setPickOnBounds(false);
        return pane;
    }

    public void addLadderOrSnake(int sourceRow, int sourceColumn, int targetRow, int targetColumn, boolean ladderOrSnake) {
        Pane sourceCell = getNodeAtIndex((size - 1) - sourceRow, sourceColumn);
        Pane targetCell = getNodeAtIndex((size - 1) - targetRow, targetColumn);
        drawLadderOrSnake(sourceCell, targetCell, ladderOrSnake);
    }

    private Pane getNodeAtIndex(int row, int column) {
        for (Node child : cellsPane.getChildren()) {
            if (child.getId() != null) {
                if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column) {
                    return (Pane) child;
                }
            }
        }
        return null;
    }

    private void drawLadderOrSnake(Pane sourcePosition, Pane targetPosition, boolean ladderOrSnake) {
        Node ladder = createLadderOrSnake(sourcePosition, targetPosition, ladderOrSnake);
        snakesAndLaddersPane.getChildren().add(ladder);
    }

    private Node createLadderOrSnake(Pane sourceCell, Pane targetCell, boolean ladderOrSnake) {
        Rectangle ladder = new Rectangle();
        ladder.setPickOnBounds(false);
        if (ladderOrSnake) {
            setImagePattern(ladder, LADDER_PATTERN_FILENAME);
        } else {
            setImagePattern(ladder, SNAKE_PATTERN_FILENAME);
        }

        HPos sourceOffset = getSourceOffset(sourceCell, targetCell);
        HPos targetOffset = getTargetOffset(sourceOffset);
        Point source = new Point(getCenterXOfCell(sourceCell, sourceOffset), getCenterYOfCell(sourceCell, VPos.TOP));
        Point target = new Point(getCenterXOfCell(targetCell, targetOffset), getCenterYOfCell(targetCell, VPos.BOTTOM));

        NumberBinding height = getDistanceBetweenTwoPoints(source, target);
        ladder.heightProperty().bind(height);

        Point coordinates = alignCenterOfRectangleToCenterOfLine(source, target, ladder);
        ladder.layoutXProperty().bind(coordinates.getX());
        ladder.layoutYProperty().bind(coordinates.getY());

        NumberBinding rotation = getAngleBetweenTwoPoints(source, target);
        ladder.rotateProperty().bind(rotation);

        return ladder;
    }

    private void setImagePattern(Rectangle rectangle, String patterFileName) {
        Image image = new Image(patterFileName);
        rectangle.setWidth(image.getWidth());
        ImagePattern imagePattern = new ImagePattern(image, 0, 0, image.getWidth(), image.getHeight(), false);
        rectangle.setFill(imagePattern);
    }

    private HPos getSourceOffset(Pane sourceCell, Pane targetCell) {
        if (sourceCell.getLayoutX() > targetCell.getLayoutX()) {
            return HPos.LEFT;
        } else if (sourceCell.getLayoutX() < targetCell.getLayoutX()) {
            return HPos.RIGHT;
        } else {
            return HPos.CENTER;
        }
    }

    private HPos getTargetOffset(HPos sourceOffset) {
        switch (sourceOffset) {
            case LEFT:
                return HPos.RIGHT;
            case CENTER:
                return HPos.CENTER;
            case RIGHT:
                return HPos.LEFT;
            default:
                return HPos.CENTER;
        }
    }

    private static class Point extends Pair<NumberBinding, NumberBinding> {

        public Point(NumberBinding k, NumberBinding v) {
            super(k, v);
        }

        public NumberBinding getX() {
            return getKey();
        }

        public NumberBinding getY() {
            return getValue();
        }
    }

    private NumberBinding getCenterXOfCell(Pane cell, HPos offset) {
        double ratio = getHPosRatio(offset);
        return add(cell.layoutXProperty(), multiply(cell.widthProperty(), ratio));
    }

    private NumberBinding getCenterYOfCell(Pane cell, VPos offset) {
        double ratio = getVPosRatio(offset);
        return add(cell.layoutYProperty(), multiply(cell.heightProperty(), ratio));
    }

    private NumberBinding getDistanceBetweenTwoPoints(final Point p1, final Point p2) {
        return new DoubleBinding() {
            {
                bind(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }

            @Override
            protected double computeValue() {
                return new Point2D(p1.getX().doubleValue(), p1.getY().doubleValue()).distance(p2.getX().doubleValue(), p2.getY().doubleValue());
            }
        };
    }

    private Point alignCenterOfRectangleToCenterOfLine(Point p1, Point p2, Rectangle rectagnle) {
        NumberBinding x = positionCenterXOfRectangleToCenterOfLine(p1.getX(), p2.getX(), rectagnle);
        NumberBinding y = positionCenterYOfRectangleToCenterOfLine(p1.getY(), p2.getY(), rectagnle);
        return new Point(x, y);
    }

    private NumberBinding positionCenterXOfRectangleToCenterOfLine(NumberBinding x1, NumberBinding x2, Rectangle rectangle) {
        return subtract(add(x1, divide(subtract(x2, x1), 2)), divide(rectangle.widthProperty(), 2));
    }

    private NumberBinding positionCenterYOfRectangleToCenterOfLine(NumberBinding y1, NumberBinding y2, Rectangle rectangle) {
        return subtract(add(y1, divide(subtract(y2, y1), 2)), divide(rectangle.heightProperty(), 2));
    }

    private NumberBinding getAngleBetweenTwoPoints(final Point p1, final Point p2) {
        return new DoubleBinding() {
            {
                bind(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }

            @Override
            protected double computeValue() {
                return angle(p1.getX().doubleValue(), p1.getY().doubleValue(), p2.getX().doubleValue(), p2.getY().doubleValue());
            }
        };
    }

    private double getHPosRatio(HPos pos) {
        switch (pos) {
            case LEFT:
                return 0.25;
            case CENTER:
                return 0.5;
            case RIGHT:
                return 0.75;
            default:
                return 0.5;
        }
    }

    private double getVPosRatio(VPos pos) {
        switch (pos) {
            case BOTTOM:
                return 0.75;
            case CENTER:
                return 0.5;
            case TOP:
                return 0.25;
            default:
                return 0.5;
        }
    }

    private static double angle(double x1, double y1, double x2, double y2) {
        double deltaY = y2 - y1;
        double deltaX = x2 - x1;
        return 90 + Math.toDegrees(Math.atan2(deltaY, deltaX));
    }
}
