package com.buffe.mariokarttimetracker.data.model



// Strecke mit fester ID und Name

enum class Track( val id: Int,  // Eindeutige ID für die Strecke
    val displayName: String    // Name der Strecke
){
    // Pilz-Cup
    MARIO_KART_STADION(1, "Mario Kart-Stadion"),
    WASSERPARK(2, "Wasserpark"),
    ZUCKERSUSSER_CANYON(3, "Zuckersüßer Canyon"),
    STEINBLOCK_RUINEN(4, "Steinblock-Ruinen"),

    // Blumen-Cup
    MARIOS_PISTE(5, "Marios Piste"),
    TOADS_HAFENSTADT(6, "Toads Hafenstadt"),
    GRUSELWUSEL_VILLA(7, "Gruselwusel-Villa"),
    SHY_GUYS_WASSERFALLE(8, "Shy Guys Wasserfälle"),

    // Stern-Cup
    SONNENFLUGHAFEN(9, "Sonnenflughafen"),
    DELFINLAGUNE(10, "Delfinlagune"),
    DISCODROM(11, "Discodrom"),
    WARIO_ABFAHRT(12, "Wario-Abfahrt"),

    // Spezial-Cup
    WOLKENSTRASSE(13, "Wolkenstraße"),
    KNOCHENTROCKENE_DUNEN(14, "Knochentrockene Dünen"),
    BOWSERS_FESTUNG(15, "Bowsers Festung"),
    REGENBOGEN_BOULEVARD(16, "Regenbogen-Boulevard"),

    // Panzer-Cup
    KUHMUH_WEIDE_WII(17, "Kuhmuh-Weide (Wii)"),
    MARIOS_PISTE_GBA(18, "Marios Piste (GBA)"),
    CHEEP_CHEEP_STRAND_DS(19, "Cheep-Cheep-Strand (DS)"),
    TOADS_AUTOBAHN_N64(20, "Toads Autobahn (N64)"),

    // Bananen-Cup
    STAUBTROCKENE_WUSTE_GCN(21, "Staubtrockene Wüste (GCN)"),
    DONUT_EBENE_3_SNES(22, "Donut-Ebene 3 (SNES)"),
    KONIGLICHE_RENNPISTE_N64(23, "Königliche Rennpiste (N64)"),
    DK_DSCHUNGEL_3DS(24, "DK Dschungel (3DS)"),

    // Blatt-Cup
    WARIO_ARENA_DS(25, "Wario-Arena (DS)"),
    SORBET_LAND_GCN(26, "Sorbet-Land (GCN)"),
    INSTRUMENTALPISTE_3DS(27, "Instrumentalpiste (3DS)"),
    YOSHI_TAL_N64(28, "Yoshi-Tal (N64)"),

    // Blitz-Cup
    TICK_TACK_TRAUMA_DS(29, "Tick-Tack-Trauma (DS)"),
    ROHRENRASEREI_3DS(30, "Röhrenraserei (3DS)"),
    VULKANGROLLEN_WII(31, "Vulkangrollen (Wii)"),
    REGENBOGEN_BOULEVARD_N64(32, "Regenbogen-Boulevard (N64)"),

    // Ei-Cup (DLC 1)
    YOSHIS_PISTE_GCN(33, "Yoshis Piste (GCN)"),
    EXCITEBIKE_STADION(34, "Excitebike-Stadion"),
    DRACHEN_DRIFTWAY(35, "Drachen-Driftway"),
    MUTE_CITY(36, "Mute City"),

    // Triforce-Cup (DLC 1)
    WARIOS_GOLDMINE_WII(37, "Warios Goldmine (Wii)"),
    REGENBOGEN_BOULEVARD_SNES(38, "Regenbogen-Boulevard (SNES)"),
    EISKREM_TRACK_ENTITY(39, "Eiskrem- TrackEntity"),
    HYRULE_PISTE(40, "Hyrule-Piste"),

    // Crossing-Cup (DLC 2)
    BABY_PARK_GCN(41, "Baby-Park (GCN)"),
    KASELAND_GBA(42, "Käseland (GBA)"),
    WILD_WOODS(43, "Wild-Woods"),
    ANIMAL_CROSSING(44, "Animal Crossing"),

    // Glocken-Cup (DLC 2)
    KOOPA_GROSSSTADTFIEBER_3DS(45, "Koopa-Großstadtfieber (3DS)"),
    RIBBON_ROAD_GBA(46, "Ribbon Road (GBA)"),
    MARIOS_METRO(47, "Marios Metro"),
    BIG_BLUE(48, "Big Blue"),

    // Goldener Turbo-Cup
    PARIS_PARCOURS_TOUR(49, "Paris-Parcours (Tour)"),
    TOADS_PISTE_3DS(50, "Toads Piste (3DS)"),
    SCHOKO_SUMPF_N64(51, "Schoko-Sumpf (N64)"),
    KOKOS_PROMENADE_WII(52, "Kokos-Promenade (Wii)"),

    // Glückskatzen-Cup
    TOKIO_TEMPOTOUR_TOUR(53, "Tokio-Tempotour (Tour)"),
    PILZ_PASS_DS(54, "Pilz-Pass (DS)"),
    HIMMELSGARTEN_GBA(55, "Himmelsgarten (GBA)"),
    NINJA_DOJO_TOUR(56, "Ninja-Dojo (Tour)"),

    // Rüben-Cup
    NEW_YORK_SPEEDWAY_TOUR(57, "New York-Speedway (Tour)"),
    MARIOS_PISTE_3_SNES(58, "Marios Piste 3 (SNES)"),
    KALIMARI_WUSTE_N64(59, "Kalimari-Wüste (N64)"),
    WALUIGI_FLIPPER_DS(60, "Waluigi-Flipper (DS)"),

    // Propeller-Cup
    SYDNEY_SPRITZTOUR_TOUR(61, "Sydney-Spritztour (Tour)"),
    SCHNEELAND_GBA(62, "Schneeland (GBA)"),
    PILZ_SCHLUCHT_WII(63, "Pilz-Schlucht (Wii)"),
    EISCREME_ESKAPADE(64, "Eiscreme-Eskapade"),

    // Fels-Cup
    LONDON_TOUR_TOUR(65, "London-Tour (Tour)"),
    BUU_HUU_TAL_GBA(66, "Buu-Huu-Tal (GBA)"),
    BERGPHAD_3DS(67, "Bergpfad (3DS)"),
    BLATTERWALD_WII(68, "Blätterwald (Wii)"),

    // Mond-Cup
    BERLIN_BEI_NACHT_TOUR(69, "Berlin bei Nacht (Tour)"),
    PEACHS_SCHLOSSGARTEN_DS(70, "Peachs Schlossgarten (DS)"),
    MERRY_MOUNTAIN(71, "Merry Mountain"),
    REGENBOGEN_BOULEVARD_3DS(72, "Regenbogen-Boulevard (3DS)"),

    // Frucht-Cup
    AMSTERDAM_DRIFT_TOUR(73, "Amsterdam-Drift (Tour)"),
    RIVERSIDE_PARK_GBA(74, "Riverside Park (GBA)"),
    DKS_SCHNEELAND_WII(75, "DKs Schneeland (Wii)"),
    YOSHIS_INSEL(76, "Yoshis Insel"),

    // Bumerang-Cup
    BANGKOK_RUSH_TOUR(77, "Bangkok Rush (Tour)"),
    MARIOS_PISTE_DS(78, "Marios Piste (DS)"),
    WALUIGI_FLIPPER_GCN(79, "Waluigi-Flipper (GCN)"),
    SINGAPUR_SPEEDWAY_TOUR(80, "Singapur-Speedway (Tour)"),

    // Feder-Cup
    ATHEN_ABENTEUER_TOUR(81, "Athen-Abenteuer (Tour)"),
    DAISYS_KREUZFAHRT_GCN(82, "Daisys Kreuzfahrt (GCN)"),
    MONDBLICKSTRASSE_WII(83, "Mondblickstraße (Wii)"),
    BADEWANNEN_PARCOURS(84, "Badewannen-Parcours"),

    // Doppelkirschen-Cup
    LOS_ANGELES_LAPS_TOUR(85, "Los Angeles Laps (Tour)"),
    SONNENUNTERGANGS_WUSTE_GBA(86, "Sonnenuntergangs-Wüste (GBA)"),
    KOOPA_KAP_WII(87, "Koopa-Kap (Wii)"),
    VANCOUVER_WILDPFAD_TOUR(88, "Vancouver-Wildpfad (Tour)"),

    // Eichel-Cup
    ROM_RAMBAZAMBA_TOUR(89, "Rom-Rambazamba (Tour)"),
    DK_BERGLAND_GCN(90, "DK-Bergland (GCN)"),
    DAISYS_PISTE_WII(91, "Daisys Piste (Wii)"),
    PIRANHA_PFLANZEN_BUCHT_TOUR(92, "Piranha-Pflanzen-Bucht (Tour)"),

    // Eichel-Cup
    STADTRUNDFAHRT_MADRID_TOUR(93, "Stadtrundfahrt Madrid (Tour)"),
    ROSALINAS_EISPLANET_3DS(94, "Rosalinas Eisplanet (3DS)"),
    BOWSERS_FESTUNG_3_SNES(95, "Bowsers Festung 3 (SNES)"),
    REGENBOGEN_BOULEVARD_WII(96, "Regenbogen-Boulevard (Wii)");
companion object{
    fun getById(id: Int): Track {
        return Track.values().find { it.id == id }
            ?: throw IllegalArgumentException("Keine Strecke mit der ID $id gefunden.")
    }
}

}