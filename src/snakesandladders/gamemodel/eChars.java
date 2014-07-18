/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakesandladders.gamemodel;

/**
 *
 * @author Noam
 */
public enum eChars {

    SNAKE {
                int m_indexToUp;

                void Init(int o_IndexToUp) {
                    m_indexToUp = o_IndexToUp;
                }

                int GetIndexToUp() {
                    return m_indexToUp;
                }
            },
    LADDER {
                int m_IndexToDown;

                void Init(int o_IndexToDown) {
                    m_IndexToDown = o_IndexToDown;
                }

                int GetIndexToDown() {
                    return m_IndexToDown;
                }
            },
    WIN {
                int m_IndexToWin;

                void Init(int o_IndexToWin) {
                    m_IndexToWin = o_IndexToWin;
                }

                int GetIndexToWin() {
                    return m_IndexToWin;
                }
            }, NONE;
}
