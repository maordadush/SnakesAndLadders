<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<snakesandladders name="Game 0" number_of_soldiers="2" current_player="Computer0">
    <players>
        <player type="COMPUTER" name="Computer0"/>
        <player type="COMPUTER" name="Computer1"/>
    </players>
    <board size="5">
        <cells>
            <cell number="1">
                <soldiers playerName="Computer0" count="4"/>
                <soldiers playerName="Computer1" count="4"/>
            </cell>
        </cells>
        <ladders>
            <ladder from="8" to="19"/>
            <ladder from="12" to="21"/>
        </ladders>
        <snakes>
            <snake from="22" to="3"/>
            <snake from="23" to="20"/>
        </snakes>
    </board>
</snakesandladders>
