package com.snakegame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;


public class SnakeNodeManager {

    //HashSet containing every position in format XXXYYY
    //Meaning to get X you divide the whole number by 1000 and for Y modular divide by 1000
    HashSet<Integer> positions = new HashSet<>();

    //FIFO structure, implemented with a LinkedList
    private final LinkedList<SnakeNodeWrapper> nodeWrappers = new LinkedList<>();

    private Pane root;
    private boolean toSpawn = false;
    private SnakeHeadController.direction currHeadDirection;

    private int prevHeadPos;
    private int currHeadPos;

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
    //all movement of the snake is implemented by removing the last node and putting it on the previous pos of head (prevHeadPos)
    //this is why loadNode() adds the new node last
    public boolean updateNodes(int headX, int headY) throws IOException {
        this.currHeadPos = headX*1000 + headY;
        if (toSpawn) {
            loadNode();
            toSpawn = false;
        }
        if (!nodeWrappers.isEmpty()) {
            //the last node is removed and appendedFirst, because it is the only node that should change its position
            //the node's position is removed from the positions set before being updated and added again, unless a new node will be spawned. In that case, the position is kept,
            //because the new node will occupy it
            SnakeNodeWrapper currNode = nodeWrappers.removeLast();
            if (!toSpawn) {
                positions.remove((int) (currNode.node().getLayoutX()*1000+currNode.node().getLayoutY()));
            }
            nodeWrappers.addFirst(currNode);
            setNodePosition(currNode.node(), prevHeadPos);
            positions.add(prevHeadPos);
            resolveAndSetNodeImageView();
        }
        prevHeadPos = this.currHeadPos;
        return !positions.contains(this.currHeadPos);
    }

    //TurnProcessor requires positions in order to add the position of the head
    //AND to NOT spawn the apple under the snake

    public HashSet<Integer> getPositions() {
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
                int headX = currHeadPos/1000, headY = currHeadPos%1000;
                int diffX, diffY;

                if (isHeadVertical) {
                    diffX = (int) nextNode.node().getLayoutX() - (int) currNode.node().getLayoutX();
                    diffY = headY - (int) currNode.node().getLayoutY();
                } else {
                    diffX = headX - (int) currNode.node().getLayoutX();
                    diffY = (int) nextNode.node().getLayoutY() - (int) currNode.node().getLayoutY();
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

    private void setNodePosition(Parent node, int  newPos) {
        node.setLayoutX(newPos/1000);
        node.setLayoutY(newPos%1000);
    }

    //when loadNode() is called it places the new node at the end of the list, so that it is the first that gets updated in updateNodes()
    private void loadNode() throws IOException {
        FXMLLoader nodeLoader = new FXMLLoader(getClass().getResource("/com/snakegame/SnakeNode.fxml"));
        SnakeNodeWrapper node = new SnakeNodeWrapper(nodeLoader.load(), nodeLoader.getController());
        nodeWrappers.add(node);
        root.getChildren().add(node.node());
    }
}
