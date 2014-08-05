/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.xml;

/**
 *
 * @author Noam
 */
public enum eXMLLoadStatus {

    LOAD_SUCCESS, XML_FILE_NOT_FOUND, XSD_FILE_NOT_FOUND, PLAYERS_DUPLICATE_NAME, SOLDIERS_TO_WIN_ERROR,
    GENERAL_ERROR, PLAYER_TYPE, PLAYER_NAME_ERROR, WRONG_GAME_INDEX, CURR_TURN_PLAYER_ERROR, ADD_PLAYER_ERROR,
    NOT_VALID_XML, BOARD_SIZE_ERROR, WINNER_ERROR, SNAKES_LADDERS_ERROR, ILLIGAL_NUM_OF_SOLDIERS, SOLDIER_LIST_NOT_INITED;
}
