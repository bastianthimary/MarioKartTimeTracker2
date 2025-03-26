package com.buffe.mariokarttimetracker.data.database

import com.buffe.mariokarttimetracker.data.model.Track

object TrackData {
    val tracks = listOf(
        // Pilz-Cup
        Track(1, "Mario Kart-Stadion"),
        Track(2, "Wasserpark"),
        Track(3, "Zuckersüßer Canyon"),
        Track(4, "Steinblock-Ruinen"),

        // Blumen-Cup
        Track(5, "Marios Piste"),
        Track(6, "Toads Hafenstadt"),
        Track(7, "Gruselwusel-Villa"),
        Track(8, "Shy Guys Wasserfälle"),

        // Stern-Cup
        Track(9, "Sonnenflughafen"),
        Track(10, "Delfinlagune"),
        Track(11, "Discodrom"),
        Track(12, "Wario-Abfahrt"),

        // Spezial-Cup
        Track(13, "Wolkenstraße"),
        Track(14, "Knochentrockene Dünen"),
        Track(15, "Bowsers Festung"),
        Track(16, "Regenbogen-Boulevard"),

        // Panzer-Cup
        Track(17, "Kuhmuh-Weide (Wii)"),
        Track(18, "Marios Piste (GBA)"),
        Track(19, "Cheep-Cheep-Strand (DS)"),
        Track(20, "Toads Autobahn (N64)"),

        // Bananen-Cup
        Track(21, "Staubtrockene Wüste (GCN)"),
        Track(22, "Donut-Ebene 3 (SNES)"),
        Track(23, "Königliche Rennpiste (N64)"),
        Track(24, "DK Dschungel (3DS)"),

        // Blatt-Cup
        Track(25, "Wario-Arena (DS)"),
        Track(26, "Sorbet-Land (GCN)"),
        Track(27, "Instrumentalpiste (3DS)"),
        Track(28, "Yoshi-Tal (N64)"),

        // Blitz-Cup
        Track(29, "Tick-Tack-Trauma (DS)"),
        Track(30, "Röhrenraserei (3DS)"),
        Track(31, "Vulkangrollen (Wii)"),
        Track(32, "Regenbogen-Boulevard (N64)"),

        // Ei-Cup (DLC 1)
        Track(33, "Yoshis Piste (GCN)"),
        Track(34, "Excitebike-Stadion"),
        Track(35, "Drachen-Driftway"),
        Track(36, "Mute City"),

        // Triforce-Cup (DLC 1)
        Track(37, "Warios Goldmine (Wii)"),
        Track(38, "Regenbogen-Boulevard (SNES)"),
        Track(39, "Eiskrem-Track"),
        Track(40, "Hyrule-Piste"),

        // Crossing-Cup (DLC 2)
        Track(41, "Baby-Park (GCN)"),
        Track(42, "Käseland (GBA)"),
        Track(43, "Wild-Woods"),
        Track(44, "Animal Crossing"),

        // Glocken-Cup (DLC 2)
        Track(45, "Koopa-Großstadtfieber (3DS)"),
        Track(46, "Ribbon Road (GBA)"),
        Track(47, "Marios Metro"),
        Track(48, "Big Blue"),

        // Goldener Turbo-Cup (Booster-Streckenpass Welle 1)
        Track(49, "Paris-Parcours (Tour)"),
        Track(50, "Toads Piste (3DS)"),
        Track(51, "Schoko-Sumpf (N64)"),
        Track(52, "Kokos-Promenade (Wii)"),

        // Glückskatzen-Cup (Booster-Streckenpass Welle 1)
        Track(53, "Tokio-Tempotour (Tour)"),
        Track(54, "Pilz-Pass (DS)"),
        Track(55, "Himmelsgarten (GBA)"),
        Track(56, "Ninja-Dojo (Tour)"),

        // Rüben-Cup (Booster-Streckenpass Welle 2)
        Track(57, "New York-Speedway (Tour)"),
        Track(58, "Marios Piste 3 (SNES)"),
        Track(59, "Kalimari-Wüste (N64)"),
        Track(60, "Waluigi-Flipper (DS)"),

        // Propeller-Cup (Booster-Streckenpass Welle 2)
        Track(61, "Sydney-Spritztour (Tour)"),
        Track(62, "Schneeland (GBA)"),
        Track(63, "Pilz-Schlucht (Wii)"),
        Track(64, "Eiscreme-Eskapade"),

        // Fels-Cup (Booster-Streckenpass Welle 3)
        Track(65, "London-Tour (Tour)"),
        Track(66, "Buu-Huu-Tal (GBA)"),
        Track(67, "Bergpfad (3DS)"),
        Track(68, "Blätterwald (Wii)"),

        // Mond-Cup (Booster-Streckenpass Welle 3)
        Track(69, "Berlin bei Nacht (Tour)"),
        Track(70, "Peachs Schlossgarten (DS)"),
        Track(71, "Merry Mountain"),
        Track(72, "Regenbogen-Boulevard (3DS)"),

        // Frucht-Cup (Booster-Streckenpass Welle 4)
        Track(73, "Amsterdam-Drift (Tour)"),
        Track(74, "Riverside Park (GBA)"),
        Track(75, "DKs Schneeland (Wii)"),
        Track(76, "Yoshis Insel"),

        // Bumerang-Cup (Booster-Streckenpass Welle 4)
        Track(77, "Bangkok Rush (Tour)"),
        Track(78, "Marios Piste (DS)"),
        Track(79, "Waluigi-Flipper (GCN)"),
        Track(80, "Singapur-Speedway (Tour)"),

        // Feder-Cup (Booster-Streckenpass Welle 5)
        Track(81, "Athen-Abenteuer (Tour)"),
        Track(82, "Daisys Kreuzfahrt (GCN)"),
        Track(83, "Mondblickstraße (Wii)"),
        Track(84, "Badewannen-Parcours"),

        // Doppelkirschen-Cup (Booster-Streckenpass Welle 5)
        Track(85, "Los Angeles Laps (Tour)"),
        Track(86, "Sonnenuntergangs-Wüste (GBA)"),
        Track(87, "Koopa-Kap (Wii)"),
        Track(88, "Vancouver-Wildpfad (Tour)"),
        //Eichel-Cup
        Track(89, "Rom-Rambazamba (Tour)"),
        Track(90, "DK-Bergland (GCN)"),
        Track(91, "Daisys Piste (Wii)"),
        Track(92, "Piranha-Pflanzen-Bucht (Tour)"),
        //Eichel-Cup
        Track(93, "Stadtrundfahrt Madrid (Tour)"),
        Track(94, "Rosalinas Eisplanet (3DS)"),
        Track(95, "Bowsers Festung 3 (SNES)"),
        Track(96, "Regenbogen-Boulevard (Wii)"),
    )
}


