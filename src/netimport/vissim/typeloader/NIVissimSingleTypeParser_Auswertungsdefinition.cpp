/****************************************************************************/
// Eclipse SUMO, Simulation of Urban MObility; see https://eclipse.org/sumo
// Copyright (C) 2001-2018 German Aerospace Center (DLR) and others.
// This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v2.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v20.html
// SPDX-License-Identifier: EPL-2.0
/****************************************************************************/
/// @file    NIVissimSingleTypeParser_Auswertungsdefinition.cpp
/// @author  Daniel Krajzewicz
/// @author  Michael Behrisch
/// @date    Fri, 21 Mar 2003
/// @version $Id$
///
//
/****************************************************************************/


// ===========================================================================
// included modules
// ===========================================================================
#ifdef _MSC_VER
#include <windows_config.h>
#else
#include <config.h>
#endif

#include <iostream>
#include <utils/common/TplConvert.h>
#include "../NIImporter_Vissim.h"
#include "../tempstructs/NIVissimSource.h"
#include "NIVissimSingleTypeParser_Auswertungsdefinition.h"


// ===========================================================================
// method definitions
// ===========================================================================
NIVissimSingleTypeParser_Auswertungsdefinition::NIVissimSingleTypeParser_Auswertungsdefinition(NIImporter_Vissim& parent)
    : NIImporter_Vissim::VissimSingleTypeParser(parent) {}


NIVissimSingleTypeParser_Auswertungsdefinition::~NIVissimSingleTypeParser_Auswertungsdefinition() {}


bool
NIVissimSingleTypeParser_Auswertungsdefinition::parse(std::istream& from) {
    std::string id;
    from >> id; // "typ"

    if (id == "DATENBANK") { // !!! unverified
        return true;
    }

    std::string type = myRead(from);
    if (type == "abfluss") {
        while (type != "signalgruppe") {
            type = myRead(from);
        }
        while (type != "DATAEND") {
            type = readEndSecure(from, "messung");
        }
    } else if (type == "vbv") {} else if (type == "dichte") {} else if (type == "emissionen") {} else if (type == "fzprot") {} else if (type == "spwprot") {} else if (type == "segment") {
        while (type != "konfdatei") {
            type = myRead(from);
        }
    } else if (type == "wegeausw") {} else if (type == "knoten") {} else if (type == "konvergenz") {
        while (type != "zeit") {
            type = myRead(from);
        }
    }
    return true;
}



/****************************************************************************/

