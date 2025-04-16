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

    // Ei-Cup (DLC 1)
    YOSHIS_PISTE_GCN(17, "Yoshis Piste (GCN)"),
    EXCITEBIKE_STADION(18, "Excitebike-Stadion"),
    GROSSE_DRACHENMAUER(19, "Große Drachenmauer"),
    MUTE_CITY(20, "Mute City"),

    // Crossing-Cup (DLC 2)
    BABY_PARK_GCN(21, "Baby-Park (GCN)"),
    KASELAND_GBA(22, "Käseland (GBA)"),
    WILDER_WIPFELWEG(23, "Wilder Wipfelweg"),
    ANIMAL_CROSSING_DORF(24, "Animal Crossing Dorf"),
    // Panzer-Cup
    KUHMUH_WEIDE_WII(25, "Kuhmuh-Weide (Wii)"),
    MARIOS_PISTE_GBA(26, "Marios Piste (GBA)"),
    CHEEP_CHEEP_STRAND_DS(27, "Cheep-Cheep-Strand (DS)"),
    TOADS_AUTOBAHN_N64(28, "Toads Autobahn (N64)"),

    // Bananen-Cup
    STAUBTROCKENE_WUSTE_GCN(29, "Staubtrockene Wüste (GCN)"),
    DONUT_EBENE_3_SNES(30, "Donut-Ebene 3 (SNES)"),
    KONIGLICHE_RENNPISTE_N64(31, "Königliche Rennpiste (N64)"),
    DK_DSCHUNGEL_3DS(32, "DK Dschungel (3DS)"),

    // Blatt-Cup
    WARIO_ARENA_DS(33, "Wario-Arena (DS)"),
    SORBET_LAND_GCN(34, "Sorbet-Land (GCN)"),
    INSTRUMENTALPISTE_3DS(35, "Instrumentalpiste (3DS)"),
    YOSHI_TAL_N64(36, "Yoshi-Tal (N64)"),

    // Blitz-Cup
    TICK_TACK_TRAUMA_DS(37, "Tick-Tack-Trauma (DS)"),
    ROHRENRASEREI_3DS(38, "Röhrenraserei (3DS)"),
    VULKANGROLLEN_WII(39, "Vulkangrollen (Wii)"),
    REGENBOGEN_BOULEVARD_N64(40, "Regenbogen-Boulevard (N64)"),

    // Triforce-Cup (DLC 1)
    WARIOS_GOLDMINE_WII(41, "Warios Goldmine (Wii)"),
    REGENBOGEN_BOULEVARD_SNES(42, "Regenbogen-Boulevard (SNES)"),
    POLARKREIS_PARCOURS(43, "Polarkreis-Parcours"),
    HYRULE_PISTE(44, "Hyrule-Piste"),



    // Glocken-Cup (DLC 2)
    KOOPA_GROSSSTADTFIEBER_3DS(45, "Koopa-Großstadtfieber (3DS)"),
    PARTY_STRASSE_GBA(46, "Party Straße (GBA)"),
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
    WOLKENPISTE_GBA(55, "Wolkenpiste (GBA)"),
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
    GEBIRGSPFAD_3DS(67, "Gebirgspfad (3DS)"),
    BLATTERWALD_WII(68, "Blätterwald (Wii)"),

    // Mond-Cup
    PFLASTER_VON_BERLIN_TOUR(69, "Pflaster von Berlin (Tour)"),
    PEACHS_SCHLOSSGARTEN_DS(70, "Peachs Schlossgarten (DS)"),
    BERGBESCHERUNG_TOUR(71, "Bergscherung (Tour)"),
    REGENBOGEN_BOULEVARD_3DS(72, "Regenbogen-Boulevard (3DS)"),

    // Frucht-Cup
    AUSFAHRT_AMSTERDAM_TOUR(73, "Ausfahrt Amsterdam (Tour)"),
    FLUSSUFER_PARK_GBA(74, "Flussufer Park (GBA)"),
    DK_SKIKANE_WII(75, "DKs Skikane (Wii)"),
    YOSHIS_EILAND(76, "Yoshis Eiland"),

    // Bumerang-Cup
    BANGKOK_ABENDROT_TOUR(77, "Bangkok Abendrot (Tour)"),
    MARIOS_PISTE_DS(78, "Marios Piste (DS)"),
    WALUIGI_ARENA_GCN(79, "Waluigi-Flipper (GCN)"),
    UEBERHOLSPUR_SINGAPUR_TOUR(80, "Überholspur Singapur (Tour)"),

    // Feder-Cup
    ATHEN_AUF_ABWEGEN_TOUR(81, "Athen auf Abwegen (Tour)"),
    DAISYS_DAMPFER_GCN(82, "Daisys Dampfer (GCN)"),
    MONDBLICKSTRASSE_WII(83, "Mondblickstraße (Wii)"),
    BAD_PARCOURS(84, "Badewannen-Parcours"),

    // Doppelkirschen-Cup
    LOS_ANGELES_STRANDPARTIE_TOUR(85, "Los Angeles Strandpartie (Tour)"),
    SONNENUNTERGANGS_WUSTE_GBA(86, "Sonnenuntergangs-Wüste (GBA)"),
    KOOPA_KAP_WII(87, "Koopa-Kap (Wii)"),
    VANCOUVER_WILDPFAD_TOUR(88, "Vancouver-Wildpfad (Tour)"),

    // Eichel-Cup
    ROM_RAMBAZAMBA_TOUR(89, "Rom-Rambazamba (Tour)"),
    DK_BERGLAND_GCN(90, "DK-Bergland (GCN)"),
    DAISYS_PISTE_WII(91, "Daisys Piste (Wii)"),
    PIRANHA_PFLANZEN_BUCHT_TOUR(92, "Piranha-Pflanzen-Bucht (Tour)"),

    // Stachi-Cup
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