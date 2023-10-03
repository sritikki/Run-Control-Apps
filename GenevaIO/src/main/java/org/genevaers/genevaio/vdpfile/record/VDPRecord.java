package org.genevaers.genevaio.vdpfile.record;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.util.Map;
import java.util.TreeMap;

public abstract class VDPRecord {
	public final static int LONGSTRINGLEN = 256;
	public final static int SHORTSTRINGLEN = 48;
	public final static int MINSTRINGLEN = 8;

// 	static const uint16_t RECORDTYPEINVALID = 0;
// static const uint16_t RECORDTYPEVDPGENERATION = 1;
// static const uint16_t RECORDTYPEVDPGENERATION_LENGTH = 257;  //trick to make it 0101 hex so same when swapped
//                                                              //not that much data in the record only Prefix + 170
// static const uint16_t RECORDTYPEFORMATVIEWS = 2;
// static const uint16_t RECORDTYPECONTROL = 50;
// static const uint16_t RECORDTYPECONTROL_LENGTH = 632;
// static const uint16_t RECORDTYPESERVER = 100;
// static const uint16_t RECORDTYPESERVER_LENGTH = 2092;
// static const uint16_t RECORDTYPEFILE = 200;
// static const uint16_t RECORDTYPEFILE_LENGTH = 4108;
// static const uint16_t RECORDTYPEPGMFILE = 210;
// static const uint16_t RECORDTYPEPGMFILE_LENGTH = 1428;
// static const uint16_t RECORDTYPELR = 300;
// static const uint16_t RECORDTYPELR_LENGTH = 424;
// static const uint16_t RECORDTYPELRFIELD = 400;
// static const uint16_t RECORDTYPELRFIELD_LENGTH = 676;
// static const uint16_t RECORDTYPELRINDEX = 500;
// static const uint16_t RECORDTYPELRINDEX_LENGTH = 100;
// static const uint16_t RECORDTYPEJOINSTEPV10 = 600;
// static const uint16_t RECORDTYPEJOINSTEP = 601;
// static const uint16_t RECORDTYPEJOINSTEP_LENGTH = 692;
// static const uint16_t RECORDTYPEJOINTARGETSET = 650;
// static const uint16_t RECORDTYPEJOINGENMAP = 651;
// static const uint16_t RECORDTYPECALLPARM = 700;    // ignoring this one since it is not used
// static const uint16_t RECORDTYPEEXTRACTOUTPUTFILE = 800;
// static const uint16_t RECORDTYPEEXTRACTRECORDFILE = 801;
// static const uint16_t RECORDTYPEEXTRACTRECORDFILELENGTH = 55; //Variable length
// static const uint16_t RECORDTYPEOUTPUTFILE_LENGTH = 4056; // used for 1600 record too
// static const uint16_t RECORDTYPEVIEW = 1000;
// static const uint16_t RECORDTYPEVIEW_LENGTH = 1088;
// //static const uint16_t RECORDTYPESUMMARYRECEXIT   = 1100;
// //static const uint16_t RECORDTYPESUMMARYRECLOGIC = 120;
// static const uint16_t RECORDTYPESUMMARYRECLONGLOGIC = 1201;
// static const uint16_t RECORDTYPELOGICTEXT_LENGTH = 296; // 1200, 2200, 3200 and 4200
// static const uint16_t RECORDTYPESUMMARYRECLT = 1210;
// static const uint16_t RECORDTYPEVIEWLINES_LENGTH = 360; //1300 and 1400
// static const uint16_t RECORDTYPETITLELINES = 1300;
// static const uint16_t RECORDTYPEFOOTERLINES = 1400;
// static const uint16_t RECORDTYPESUMMARYOUTPUTFILE = 1600;
// static const uint16_t RECORDTYPEEXTRACTTARGETSET = 1650;
// static const uint16_t RECORDTYPECOLUMNRECORD = 2000;
// static const uint16_t RECORDTYPECOLUMNRECORD_LENGTH = 1724;
// //static const uint16_t RECORDTYPESUMMARYCOLEXIT   = 2100;
// //static const uint16_t RECORDTYPESUMMARYCOLLOGIC = 2200;
// static const uint16_t RECORDTYPESUMMARYCOLLONGLOGIC = 2201;
// static const uint16_t RECORDTYPESUMMARYCOLLT = 2210;
// static const uint16_t RECORDTYPESORTKEYATTR = 2300;
// static const uint16_t RECORDTYPESORTKEYATTR_LENGTH = 336;
// static const uint16_t RECORDTYPEVIEWLRFILE = 3000;
// static const uint16_t RECORDTYPEVIEWLRFILE_LENGTH = 308;
// //static const uint16_t RECORDTYPELRFILERECEXIT    = 3100;
// //static const uint16_t RECORDTYPELRFILTERECLOGIC = 3200;
// static const uint16_t RECORDTYPELRFILTERECLONGLOGIC = 3201;
// static const uint16_t RECORDTYPELRFILERECLT = 3210;
// static const uint16_t RECORDTYPEEXTOUTLOGIC = 3300;
// static const uint16_t RECORDTYPEEXTOUTLONGLOGIC = 3301;
// static const uint16_t RECORDTYPEEXTOUTLT = 3310; // not needed really
// static const uint16_t RECORDTYPELRFILECOLUMN = 4000;
// static const uint16_t RECORDTYPELRFILECOLUMN_LENGTH = 340;
// //static const uint16_t RECORDTYPELRFILECOLEXIT    = 4100;
// //static const uint16_t RECORDTYPELRFILECOLLOGIC = 4200;
// static const uint16_t RECORDTYPELRFILECOLLONGLOGIC = 4201;
// static const uint16_t RECORDTYPELRFILECOLLT = 4210;


    public static final short VDP_GENERATION = 1;
	public static final short VDP_FORMAT_VIEWS = 2;
	public static final short VDP_CONTROL = 50;
	public static final short VDP_PHYSICAL_FILE = 200;
	public static final short VDP_EXIT = 210;
	public static final short VDP_LOGICAL_RECORD = 300;
	public static final short VDP_LRFIELD = 400;
	public static final short VDP_INDEX = 500;
	public static final short VDP_LOOKUP_OLD = 600;
	public static final short VDP_LOOKUP = 601;
	public static final short VDP_LOOKUP_TARGET_SET = 650;
	public static final short VDP_LOOKUP_GENMAP = 651;
	public static final short VDP_EXTRACT_OUTPUT_FILE = 800;
	public static final short VDP_EXTRACT_RECORD_FILE = 801;
	public static final short VDP_VIEW = 1000;
	public static final short VDP_FORMAT_FILTER_OLD = 1200;
	public static final short VDP_FORMAT_FILTER_LOGIC = 1201;
	public static final short VDP_FORMAT_FILTER_STACK = 1210;
	public static final short VDP_HEADER = 1300;
	public static final short VDP_FOOTER = 1400;
	public static final short VDP_FORMAT_OUTPUT_FILE = 1600;
	public static final short VDP_EXTRACT_TARGET_SET = 1650;
	public static final short VDP_COLUMN = 2000;
	public static final short VDP_COLUMN_CALCULATION_OLD = 2200;
	public static final short VDP_COLUMN_CALCULATION = 2201;
	public static final short VDP_COLUMN_CALCULATION_LT = 2210;
	public static final short VDP_SORT_KEY = 2300;
	public static final short VDP_VIEW_SOURCE = 3000;
	public static final short VDP_EXTRACT_FILTER_OLD = 3200;
	public static final short VDP_EXTRACT_FILTER = 3201;
	public static final short VDP_EXTRACT_FILTER_LT = 3210; //Need these old record types to support legacy VDPs
	public static final short VDP_OUTPUT_LOGIC = 3301;
	public static final short VDP_COLUMN_SOURCE = 4000;
	public static final short VDP_COLUMN_LOGIC_OLD = 4200;
	public static final short VDP_COLUMN_LOGIC = 4201;
	public static final short VDP_COLUMN_LOGIC_LT = 4210;
	
	private static Map<Short, String> typeStrings = new TreeMap<Short, String>();
	
	public VDPRecord() {

	    typeStrings.put(VDP_GENERATION,"VDP_GENERATION");
		typeStrings.put(VDP_FORMAT_VIEWS,"VDP_FORMAT_VIEWS");
		typeStrings.put(VDP_CONTROL,"VDP_CONTROL");
		typeStrings.put(VDP_PHYSICAL_FILE,"VDP_PHYSICAL_FILE");
		typeStrings.put(VDP_EXIT,"VDP_EXIT");
		typeStrings.put(VDP_LOGICAL_RECORD,"VDP_LOGICAL_RECORD");
		typeStrings.put(VDP_LRFIELD,"VDP_LRFIELD");
		typeStrings.put(VDP_INDEX,"VDP_INDEX");
		typeStrings.put(VDP_LOOKUP_OLD,"VDP_LOOKUP_OLD");
		typeStrings.put(VDP_LOOKUP,"VDP_LOOKUP");
		typeStrings.put(VDP_LOOKUP_TARGET_SET,"VDP_LOOKUP_TARGET_SET");
		typeStrings.put(VDP_LOOKUP_GENMAP,"VDP_LOOKUP_GENMAP");
		typeStrings.put(VDP_EXTRACT_OUTPUT_FILE,"VDP_EXTRACT_OUTPUT_FILE");
		typeStrings.put(VDP_EXTRACT_RECORD_FILE,"VDP_EXTRACT_RECORD_FILE");
		typeStrings.put(VDP_VIEW,"VDP_VIEW");
		typeStrings.put(VDP_FORMAT_FILTER_OLD,"VDP_FORMAT_FILTER_OLD");
		typeStrings.put(VDP_FORMAT_FILTER_LOGIC,"VDP_FORMAT_FILTER_LOGIC");
		typeStrings.put(VDP_FORMAT_FILTER_STACK,"VDP_FORMAT_FILTER_STACK");
		typeStrings.put(VDP_HEADER,"VDP_HEADER");
		typeStrings.put(VDP_FOOTER,"VDP_FOOTER");
		typeStrings.put(VDP_FORMAT_OUTPUT_FILE,"VDP_FORMAT_OUTPUT_FILE");
		typeStrings.put(VDP_EXTRACT_TARGET_SET,"VDP_EXTRACT_TARGET_SET");
		typeStrings.put(VDP_COLUMN,"VDP_COLUMN");
		typeStrings.put(VDP_COLUMN_CALCULATION_OLD,"VDP_COLUMN_CALCULATION_OLD");
		typeStrings.put(VDP_COLUMN_CALCULATION,"VDP_COLUMN_CALCULATION");
		typeStrings.put(VDP_COLUMN_CALCULATION_LT,"VDP_COLUMN_CALCULATION_LT");
		typeStrings.put(VDP_SORT_KEY,"VDP_SORT_KEY");
		typeStrings.put(VDP_VIEW_SOURCE,"VDP_VIEW_SOURCE");
		typeStrings.put(VDP_EXTRACT_FILTER_OLD,"VDP_EXTRACT_FILTER_OLD");
		typeStrings.put(VDP_EXTRACT_FILTER,"VDP_EXTRACT_FILTER");
		typeStrings.put(VDP_EXTRACT_FILTER_LT,"VDP_EXTRACT_FILTER_LT"); //Need these old record types to support legacy VDPs
		typeStrings.put(VDP_OUTPUT_LOGIC,"VDP_OUTPUT_LOGIC");
		typeStrings.put(VDP_COLUMN_SOURCE,"VDP_COLUMN_SOURCE");
		typeStrings.put(VDP_COLUMN_LOGIC_OLD,"VDP_COLUMN_LOGIC_OLD");
		typeStrings.put(VDP_COLUMN_LOGIC,"VDP_COLUMN_LOGIC");
		typeStrings.put(VDP_COLUMN_LOGIC_LT,"VDP_COLUMN_LOGIC_LT");
	}
	
	public String getRecordTypeName(short type) {
		return typeStrings.get(type);
	}
	
}
