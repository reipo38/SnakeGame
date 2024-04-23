package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;


public class SnakeNodeManager {

    private final LinkedList<double[]> positions = new LinkedList<>();
    private final LinkedList<SnakeNodeWrapper> nodeWrappers = new LinkedList<>();

    private Pane root;
    private boolean toSpawn = false;
    private SnakeHeadController.direction currHeadDirection;

    public void setRoot(Pane root) {
        this.root = root;
    }

    public void setCurrHeadDirection(SnakeHeadController.direction dir) {
        currHeadDirection = dir;
    }

    public void spawnNode() {
        toSpawn = true;
    }

    public void addFirstPosition(double[] arr) {
        positions.addFirst(arr);
    }

    public boolean updateNodes() throws IOException {
        for (int i = 0; i < nodeWrappers.size(); i++) {
            setNodePosition(nodeWrappers.get(i).node(), positions.get(i + 1));
            setNodeImageView(i);
        }
        if (toSpawn) {
            loadNode(positions.getLast());
            toSpawn = false;
        } else if (positions.size() > 1) {
            positions.removeLast();
        }
        return !isCollidingHeadAndNode();
    }

    public boolean isCollidingHeadAndNode() {
        for (SnakeNodeWrapper nodeWrapper : nodeWrappers) {
            if (positions.getFirst()[0] == nodeWrapper.node().getLayoutX() && positions.getFirst()[1] == nodeWrapper.node().getLayoutY()) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<double[]> getPositions() {
        return positions;
    }

    private void setNodeImageView(int index) {
        SnakeNodeController currNodeController = nodeWrappers.get(index).controller();
        if (index == 0) {
            currNodeController.setVertical(currHeadDirection == SnakeHeadController.direction.UP || currHeadDirection == SnakeHeadController.direction.DOWN);
        } else {
            currNodeController.setVertical(nodeWrappers.get(index - 1).node().getLayoutY() - nodeWrappers.get(index).node().getLayoutY() != 0);
        }
        currNodeController.setImageView();
    }

    private void setNodePosition(Parent node, double[] newPos) {
        node.setLayoutX(newPos[0]);
        node.setLayoutY(newPos[1]);
    }

    private void loadNode(double[] newPos) throws IOException {
        FXMLLoader nodeLoader = new FXMLLoader(getClass().getResource("/com/snakegame/SnakeNode.fxml"));
        SnakeNodeWrapper nodeWrapper = new SnakeNodeWrapper(nodeLoader.load(), nodeLoader.getController());

        nodeWrappers.add(nodeWrapper);

        root.getChildren().add(nodeWrapper.node());
        setNodePosition(nodeWrapper.node(), newPos);
        setNodeImageView(nodeWrappers.size() - 1);
    }
}
