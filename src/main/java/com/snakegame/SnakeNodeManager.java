package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;


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

    //updateNodes returns false to indicate the head has collided with the body.
    public boolean updateNodes() throws IOException {
        ListIterator<SnakeNodeWrapper> nodeWrapperIterator = nodeWrappers.listIterator();
        ListIterator<double[]> positionsIterator = positions.listIterator(1);

        SnakeNodeWrapper prevNode = null;
        SnakeNodeWrapper currNode;

        while (nodeWrapperIterator.hasNext() && positionsIterator.hasNext()) {
            if (nodeWrapperIterator.hasPrevious()){
                prevNode = nodeWrapperIterator.previous();
                nodeWrapperIterator.next();
            }

            currNode = nodeWrapperIterator.next();
            assert currNode != null;
            setNodePosition(currNode.node(), positionsIterator.next());
            setNodeImageView(prevNode, currNode);
        }

        if (toSpawn) {
            loadNode(positions.getLast());
            toSpawn = false;
        } else if (positions.size() > 1) {
            positions.removeLast();
        }
        return !isCollidingHeadAndNode();
    }

    //SnakeNodeManager does not have access to head, instead it checks the node positions against the first el of positions which is ALWAYS the head position
    public boolean isCollidingHeadAndNode() {
        for (SnakeNodeWrapper nodeWrapper : nodeWrappers) {
            if (positions.getFirst()[0] == nodeWrapper.node().getLayoutX() && positions.getFirst()[1] == nodeWrapper.node().getLayoutY()) {
                return true;
            }
        }
        return false;
    }

    //TurnProcessor requires positions in order to add the position of the head
    //AND to NOT spawn the apple under the snake
    public LinkedList<double[]> getPositions() {
        return positions;
    }

    private void setNodeImageView(SnakeNodeWrapper prevNode, SnakeNodeWrapper currNode) {

        SnakeNodeController currNodeController = currNode.controller();

        currNodeController.setTurn(SnakeNodeController.turning.NONE);
        if (prevNode == null) {
            currNodeController.setVertical(currHeadDirection == SnakeHeadController.direction.UP || currHeadDirection == SnakeHeadController.direction.DOWN);
        }
        else {
            currNodeController.setVertical(prevNode.node().getLayoutY() - currNode.node().getLayoutY() != 0);
        }
        currNodeController.setImageView();
    }

    private void setNodePosition(Parent node, double[] newPos) {
        node.setLayoutX(newPos[0]);
        node.setLayoutY(newPos[1]);
    }

    private void loadNode(double[] newPos) throws IOException {
        FXMLLoader nodeLoader = new FXMLLoader(getClass().getResource("/com/snakegame/SnakeNode.fxml"));
        SnakeNodeWrapper node = new SnakeNodeWrapper(nodeLoader.load(), nodeLoader.getController());

        SnakeNodeWrapper prevNode = null;
        if (!nodeWrappers.isEmpty()) {
            prevNode = nodeWrappers.getLast();
        }

        nodeWrappers.add(node);

        root.getChildren().add(node.node());
        setNodePosition(node.node(), newPos);
        setNodeImageView(prevNode, node);
    }
}
