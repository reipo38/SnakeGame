package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;


public class SnakeNodeManager {

    //in reality only the first two elements are used
    // 0 - current head position
    // 1 - previous head position and current currNode position
    //it  contains the positions for every other node as well but the only purpose they serve is to prevent the apple from spawning under the snake
    //the logic for that is implemented in TurnProcessor
    private final LinkedList<double[]> positions = new LinkedList<>();

    //FIFO structure, implemented with a LinkedList
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
    //first thing the method does is check if a new Node is needed, if so, calls loadNode, which places the new node at the end of the list
    //removes last position because no node occupies it anymore
    //all movement of the snake is implemented by removing the last node and putting it on the previous pos of head (positions[1])
    //this is why loadNode() adds the new node last
    public boolean updateNodes() throws IOException {

        if (toSpawn) {
            loadNode();
            toSpawn = false;
        } else if (positions.size() > 1) {
            positions.removeLast();
        }
        if (!nodeWrappers.isEmpty()) {
            //the last node is removed and appendedFirst, because it is the only node that should change its position
            SnakeNodeWrapper currNode = nodeWrappers.removeLast();
            nodeWrappers.addFirst(currNode);
            setNodePosition(currNode.node(), positions.get(1));

            resolveAndSetNodeImageView();
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

    private void resolveAndSetNodeImageView() {
        SnakeNodeWrapper currNode = nodeWrappers.getFirst();
        boolean isHeadVertical = currHeadDirection == SnakeHeadController.direction.UP || currHeadDirection == SnakeHeadController.direction.DOWN;
        currNode.controller().setVertical(isHeadVertical);
        currNode.controller().setTurn(SnakeNodeController.turning.NONE);

        if (nodeWrappers.size() > 1) {
            SnakeNodeWrapper nextNode = nodeWrappers.get(1);
            if (isHeadVertical != nextNode.controller().getVertical()) {
                double headX = positions.getFirst()[0], headY = positions.getFirst()[1];
                double diffX, diffY;

                if (isHeadVertical) {
                    diffX = nextNode.node().getLayoutX() - currNode.node().getLayoutX();
                    diffY = headY - currNode.node().getLayoutY();
                } else {
                    diffX = headX - currNode.node().getLayoutX();
                    diffY = nextNode.node().getLayoutY() - currNode.node().getLayoutY();
                }

                if (diffX > 0) {
                    currNode.controller().setTurn(diffY > 0 ? SnakeNodeController.turning.RIGHTDOWN : SnakeNodeController.turning.RIGHTUP);
                } else {
                    currNode.controller().setTurn(diffY > 0 ? SnakeNodeController.turning.LEFTDOWN : SnakeNodeController.turning.LEFTUP);
                }
            }
        }
        currNode.controller().setImageView();

    }

    private void setNodePosition(Parent node, double[] newPos) {
        node.setLayoutX(newPos[0]);
        node.setLayoutY(newPos[1]);
    }

    //when loadNoad() is called it places the new node at the end of the list, so that it is the first that gets updated in updateNodes()
    private void loadNode() throws IOException {
        FXMLLoader nodeLoader = new FXMLLoader(getClass().getResource("/com/snakegame/SnakeNode.fxml"));
        SnakeNodeWrapper node = new SnakeNodeWrapper(nodeLoader.load(), nodeLoader.getController());
        nodeWrappers.add(node);
        root.getChildren().add(node.node());
    }
}
