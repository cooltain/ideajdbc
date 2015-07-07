/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc;

import java.util.List;
import java.util.Map;

import com.ideamoment.ideajdbc.server.Db;

/**
 * @author Administrator
 *
 */
public class TestZPB {

	private static int cr = 0;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IdeaJdbc.beginTransaction();
		
		List<Map<String, String>> result = IdeaJdbc.query("select f_code from t_corp_info where f_id > 1 order by f_id asc").list();
		
		//IdeaJdbc.endTransaction();
		
		System.out.println(result.size());
		System.out.println("\n\n");
		
		for(int i=0; i<result.size(); i++) {
			Map<String, String> row = result.get(i);
			String corpCode = row.get("f_code");
			
			if(corpCode.trim().equals("lietouxu") || corpCode.trim().equals("dayee")) {
				continue;
			}
			
			System.out.println(i + " >>>> " + corpCode);
			
			updateCorp(corpCode);
			
			System.out.println("Update [" + corpCode + "] successfully.");
		}
		
		System.out.println("Total: " + cr);
		
		System.out.println("\n\n");
		System.out.println("Finished!");
		IdeaJdbc.endTransaction();
	}

	private static void updateCorp(String corpCode) {
		Db db = IdeaJdbc.db("mysql" + corpCode);
		//IdeaJdbc.beginTransaction();
		long c = (Long)db.query("select count(1) from t_channel_net where f_channle_dic = 73").uniqueValue();
		if(c > 0) {
			System.out.println(corpCode);
			cr++;
			executeUpdatePostChannel();
		}
		
	}

	private static void executeUpdatePostChannel() {
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160002' where f_post_type='73/47001/47361/47391/47393'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160003' where f_post_type='73/47001/47361/47391/47399'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160004' where f_post_type='73/47001/47361/47391/47396'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160005' where f_post_type='73/47001/47361/47391/47395'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160006' where f_post_type='73/47001/47361/47391/47400'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160008' where f_post_type='73/47001/47106/47107/47115'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160001/160009' where f_post_type='73/47001/47361/47391/47392'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160014' where f_post_type='73/47001/47361/47374/47390'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160015' where f_post_type='73/47001/47361/47374/47387'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160016' where f_post_type='73/47001/47361/47374/47377'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160017' where f_post_type='73/47001/47361/47374/47384'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160018' where f_post_type='73/47001/47361/47374/47378'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160019' where f_post_type='73/47001/47361/47374/47383'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160020' where f_post_type='73/47001/47361/47374/47388'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160021' where f_post_type='73/47001/47361/47374/47386'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160022' where f_post_type='73/47001/47361/47374/47380'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160023' where f_post_type='73/47001/47361/47374/47379'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160024' where f_post_type='73/47001/47361/47374/47376'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160025' where f_post_type='73/47001/47361/47374/47381'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160026' where f_post_type='73/47001/47361/47374/47385'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160027' where f_post_type='73/47001/47361/47374/47382'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160028' where f_post_type='73/47001/47361/47374/47375'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160013/160029' where f_post_type='73/47001/47361/47374/47389'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160031' where f_post_type='73/47001/47361/47362/47366'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160032' where f_post_type='73/47001/47361/47362/47365'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160033' where f_post_type='73/47001/47361/47362/47373'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160034' where f_post_type='73/47001/47361/47362/47370'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160035' where f_post_type='73/47001/47361/47362/47363'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160036' where f_post_type='73/47001/47361/47362/47364'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160038' where f_post_type='73/47001/47361/47362/47368'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160039' where f_post_type='73/47001/47361/47362/47372'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160040' where f_post_type='73/47001/47361/47362/47369'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160041' where f_post_type='73/47001/47361/47362/47367'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160000/160030/160042' where f_post_type='73/47001/47361/47362/47371'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160045' where f_post_type='73/47001/47002/47032/47040'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160046' where f_post_type='73/47001/47002/47032/47037'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160047' where f_post_type='73/47001/47002/47032/47038'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160048' where f_post_type='73/47001/47002/47032/47036'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160049' where f_post_type='73/47001/47002/47032/47041'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160050' where f_post_type='73/47001/47002/47032/47034'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160051' where f_post_type='73/47001/47002/47032/47047'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160052' where f_post_type='73/47001/47002/47032/47035'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160054' where f_post_type='73/47001/47002/47032/47039'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160056' where f_post_type='73/47001/47002/47032/47033'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160044/160057' where f_post_type='73/47001/47002/47032/47042'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160059' where f_post_type='73/47001/47002/47048/47051'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160060' where f_post_type='73/47001/47002/47048/47050'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160061' where f_post_type='73/47001/47002/47048/47054'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160062' where f_post_type='73/47001/47002/47048/47049'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160063' where f_post_type='73/47001/47002/47048/47056'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160064' where f_post_type='73/47001/47002/47048/47055'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160066' where f_post_type='73/47001/47002/47048/47053'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160058/160067' where f_post_type='73/47001/47002/47048/47052'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160071' where f_post_type='73/47001/47002/47014/47027'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160072' where f_post_type='73/47001/47002/47014/47024'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160073' where f_post_type='73/47001/47002/47014/47026'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160074' where f_post_type='73/47001/47002/47014/47015'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160075' where f_post_type='73/47001/47002/47014/47023'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160076' where f_post_type='73/47001/47002/47014/47020'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160077' where f_post_type='73/47001/47002/47014/47017'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160078' where f_post_type='73/47001/47002/47014/47022'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160079' where f_post_type='73/47001/47002/47014/47018'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160080' where f_post_type='73/47001/47002/47014/47019'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160081' where f_post_type='73/47001/47002/47014/47016'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160068/160083' where f_post_type='73/47001/47002/47014/47021'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160092/160093' where f_post_type='73/47001/47002/47032/47045'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160092/160096' where f_post_type='73/47001/47002/47032/47046'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160098' where f_post_type='73/47001/47002/47057/47058'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160099' where f_post_type='73/47001/47002/47057/47068'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160100' where f_post_type='73/47001/47002/47057/47066'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160101' where f_post_type='73/47001/47002/47057/47061'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160102' where f_post_type='73/47001/47002/47057/47062'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160103' where f_post_type='73/47001/47002/47057/47065'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160104' where f_post_type='73/47001/47002/47057/47067'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160105' where f_post_type='73/47001/47002/47057/47063'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160106' where f_post_type='73/47001/47002/47057/47059'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160107' where f_post_type='73/47001/47002/47057/47060'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160097/160108' where f_post_type='73/47001/47002/47057/47064'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160109/160112' where f_post_type='73/47001/47002/47014/47030'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160109/160114' where f_post_type='73/47001/47002/47014/47025'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160109/160115' where f_post_type='73/47001/47002/47014/47028'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160109/160117' where f_post_type='73/47001/47002/47014/47031'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160043/160109/160120' where f_post_type='73/47001/47002/47014/47029'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160123' where f_post_type='73/47001/47002/47003/47005'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160124' where f_post_type='73/47001/47002/47003/47010'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160125' where f_post_type='73/47001/47002/47003/47006'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160126' where f_post_type='73/47001/47002/47003/47008'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160127' where f_post_type='73/47001/47002/47003/47009'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160128' where f_post_type='73/47001/47002/47003/47007'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160129' where f_post_type='73/47001/47002/47003/47004'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160130' where f_post_type='73/47001/47002/47003/47011'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160131' where f_post_type='73/47001/47002/47003/47013'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160122/160132' where f_post_type='73/47001/47002/47003/47012'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160135' where f_post_type='73/47001/47106/47134/47142'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160136' where f_post_type='73/47001/47106/47134/47138'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160137' where f_post_type='73/47001/47106/47134/47139'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160138' where f_post_type='73/47001/47106/47134/47140'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160139' where f_post_type='73/47001/47106/47134/47136'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160140' where f_post_type='73/47001/47106/47134/47143'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160141' where f_post_type='73/47001/47106/47134/47135'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160134/160142' where f_post_type='73/47001/47106/47134/47137'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160143/160151' where f_post_type='73/47001/47106/47107/47114'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160160/160161' where f_post_type='73/47001/47106/47127/47129'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160160/160162' where f_post_type='73/47001/47106/47127/47128'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160160/160163' where f_post_type='73/47001/47106/47127/47133'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160160/160164' where f_post_type='73/47001/47106/47127/47132'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160160/160165' where f_post_type='73/47001/47106/47127/47130'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160160/160166' where f_post_type='73/47001/47106/47127/47131'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160168' where f_post_type='73/47001/47106/47107/47122'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160169' where f_post_type='73/47001/47106/47107/47111'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160170' where f_post_type='73/47001/47106/47107/47118'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160171' where f_post_type='73/47001/47106/47107/47108'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160172' where f_post_type='73/47001/47106/47107/47117'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160173' where f_post_type='73/47001/47106/47107/47121'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160174' where f_post_type='73/47001/47106/47107/47123'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160175' where f_post_type='73/47001/47106/47107/47124'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160176' where f_post_type='73/47001/47106/47107/47126'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160177' where f_post_type='73/47001/47106/47107/47113'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160178' where f_post_type='73/47001/47106/47107/47116'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160179' where f_post_type='73/47001/47106/47107/47125'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160180' where f_post_type='73/47001/47106/47107/47120'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160181' where f_post_type='73/47001/47106/47107/47119'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160182' where f_post_type='73/47001/47106/47107/47112'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160183' where f_post_type='73/47001/47106/47107/47110'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160121/160167/160184' where f_post_type='73/47001/47106/47107/47109'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160187' where f_post_type='73/47001/47281/47305/47318'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160188' where f_post_type='73/47001/47281/47305/47312'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160189' where f_post_type='73/47001/47281/47305/47308'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160190' where f_post_type='73/47001/47281/47305/47317'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160191' where f_post_type='73/47001/47281/47305/47306'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160192' where f_post_type='73/47001/47281/47305/47313'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160193' where f_post_type='73/47001/47281/47305/47307'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160194' where f_post_type='73/47001/47281/47305/47316'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160195' where f_post_type='73/47001/47281/47305/47311'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160186/160196' where f_post_type='73/47001/47281/47305/47315'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160198' where f_post_type='73/47001/47281/47282/47302'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160199' where f_post_type='73/47001/47281/47282/47299'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160200' where f_post_type='73/47001/47281/47282/47297'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160201' where f_post_type='73/47001/47281/47282/47298'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160202' where f_post_type='73/47001/47281/47282/47303'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160203' where f_post_type='73/47001/47281/47282/47283'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160204' where f_post_type='73/47001/47177/47223/47226'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160205' where f_post_type='73/47001/47281/47282/47288'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160206' where f_post_type='73/47001/47281/47282/47284'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160207' where f_post_type='73/47001/47281/47282/47294'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160208' where f_post_type='73/47001/47281/47282/47292'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160209' where f_post_type='73/47001/47281/47282/47286'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160210' where f_post_type='73/47001/47281/47282/47285'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160211' where f_post_type='73/47001/47281/47282/47304'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160212' where f_post_type='73/47001/47281/47282/47287'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160213' where f_post_type='73/47001/47281/47282/47301'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160214' where f_post_type='73/47001/47281/47282/47295'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160215' where f_post_type='73/47001/47281/47282/47291'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160216' where f_post_type='73/47001/47281/47282/47300'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160217' where f_post_type='73/47001/47281/47282/47293'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160218' where f_post_type='73/47001/47281/47282/47289'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160197/160219' where f_post_type='73/47001/47281/47282/47296'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160220/160221' where f_post_type='73/47001/47281/47305/47312'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160220/160223' where f_post_type='73/47001/47281/47305/47317'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160220/160224' where f_post_type='73/47001/47281/47305/47310'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160220/160225' where f_post_type='73/47001/47281/47305/47314'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160185/160220/160226' where f_post_type='73/47001/47281/47305/47309'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160229' where f_post_type='73/47001/47402/47422/47425'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160230' where f_post_type='73/47001/47402/47422/47429'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160231' where f_post_type='73/47001/47402/47422/47431'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160232' where f_post_type='73/47001/47402/47422/47426'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160233' where f_post_type='73/47001/47402/47422/47430'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160234' where f_post_type='73/47001/47402/47422/47427'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160235' where f_post_type='73/47001/47402/47422/47423'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160236' where f_post_type='73/47001/47402/47422/47424'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160228/160237' where f_post_type='73/47001/47402/47422/47428'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160239' where f_post_type='73/47001/47402/47403/47404'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160240' where f_post_type='73/47001/47402/47403/47412'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160241' where f_post_type='73/47001/47402/47403/47406'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160242' where f_post_type='73/47001/47402/47403/47409'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160243' where f_post_type='73/47001/47402/47403/47415'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160244' where f_post_type='73/47001/47402/47403/47408'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160245' where f_post_type='73/47001/47402/47403/47411'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160246' where f_post_type='73/47001/47402/47403/47410'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160247' where f_post_type='73/47001/47402/47403/47405'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160248' where f_post_type='73/47001/47402/47403/47414'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160249' where f_post_type='73/47001/47402/47403/47421'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160250' where f_post_type='73/47001/47402/47403/47420'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160251' where f_post_type='73/47001/47402/47403/47413'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160238/160252' where f_post_type='73/47001/47402/47403/47407'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160253/160254' where f_post_type='73/47001/47402/47403/47416'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160253/160255' where f_post_type='73/47001/47402/47403/47419'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160253/160256' where f_post_type='73/47001/47402/47403/47417'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160227/160253/160257' where f_post_type='73/47001/47402/47403/47418'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160259/160260' where f_post_type='73/47001/47177/47223/47227'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160259/160261' where f_post_type='73/47001/47177/47223/47226'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160259/160262' where f_post_type='73/47001/47177/47223/47224'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160259/160263' where f_post_type='73/47001/47177/47223/47229'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160259/160264' where f_post_type='73/47001/47177/47223/47228'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160259/160265' where f_post_type='73/47001/47177/47223/47225'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160267' where f_post_type='73/47001/47177/47230/47234'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160268' where f_post_type='73/47001/47177/47230/47233'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160269' where f_post_type='73/47001/47177/47230/47235'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160270' where f_post_type='73/47001/47177/47230/47237'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160271' where f_post_type='73/47001/47177/47230/47236'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160272' where f_post_type='73/47001/47177/47230/47238'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160273' where f_post_type='73/47001/47177/47230/47232'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160274' where f_post_type='73/47001/47177/47230/47231'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160266/160275' where f_post_type='73/47001/47177/47230/47239'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160277' where f_post_type='73/47001/47177/47211/47220'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160278' where f_post_type='73/47001/47177/47211/47219'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160279' where f_post_type='73/47001/47177/47211/47214'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160280' where f_post_type='73/47001/47177/47211/47215'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160281' where f_post_type='73/47001/47177/47211/47222'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160282' where f_post_type='73/47001/47177/47211/47216'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160283' where f_post_type='73/47001/47177/47211/47218'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160284' where f_post_type='73/47001/47177/47211/47221'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160285' where f_post_type='73/47001/47177/47211/47213'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160286' where f_post_type='73/47001/47177/47211/47212'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160276/160287' where f_post_type='73/47001/47177/47211/47217'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160289' where f_post_type='73/47001/47177/47178/47198'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160290' where f_post_type='73/47001/47177/47178/47202'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160291' where f_post_type='73/47001/47177/47178/47203'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160292' where f_post_type='73/47001/47177/47178/47197'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160293' where f_post_type='73/47001/47177/47178/47205'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160294' where f_post_type='73/47001/47177/47178/47196'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160295' where f_post_type='73/47001/47177/47178/47194'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160296' where f_post_type='73/47001/47177/47178/47206'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160297' where f_post_type='73/47001/47177/47178/47200'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160298' where f_post_type='73/47001/47177/47178/47195'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160299' where f_post_type='73/47001/47177/47178/47208'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160300' where f_post_type='73/47001/47177/47178/47201'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160301' where f_post_type='73/47001/47177/47178/47199'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160288/160302' where f_post_type='73/47001/47177/47178/47193'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160304' where f_post_type='73/47001/47177/47178/47207'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160305' where f_post_type='73/47001/47177/47178/47209'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160307' where f_post_type='73/47001/47177/47178/47181'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160308' where f_post_type='73/47001/47177/47178/47179'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160309' where f_post_type='73/47001/47177/47178/47191'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160310' where f_post_type='73/47001/47177/47178/47180'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160315' where f_post_type='73/47001/47177/47178/47192'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160317' where f_post_type='73/47001/47177/47178/47186'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160319' where f_post_type='73/47001/47177/47178/47210'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160320' where f_post_type='73/47001/47177/47178/47183'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160321' where f_post_type='73/47001/47177/47178/47185'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160323' where f_post_type='73/47001/47177/47178/47184'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160325' where f_post_type='73/47001/47177/47178/47187'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160327' where f_post_type='73/47001/47177/47178/47182'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160328' where f_post_type='73/47001/47177/47230/47234'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160330' where f_post_type='73/47001/47177/47178/47188'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160303/160331' where f_post_type='73/47001/47177/47178/47189'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160333' where f_post_type='73/47001/47240/47241/47248'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160334' where f_post_type='73/47001/47240/47241/47246'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160335' where f_post_type='73/47001/47240/47241/47256'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160336' where f_post_type='73/47001/47240/47241/47255'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160337' where f_post_type='73/47001/47240/47241/47249'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160338' where f_post_type='73/47001/47240/47241/47254'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160339' where f_post_type='73/47001/47240/47241/47251'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160340' where f_post_type='73/47001/47240/47241/47242'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160341' where f_post_type='73/47001/47240/47241/47250'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160342' where f_post_type='73/47001/47240/47241/47245'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160343' where f_post_type='73/47001/47240/47241/47244'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160344' where f_post_type='73/47001/47240/47241/47243'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160345' where f_post_type='73/47001/47240/47241/47247'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160346' where f_post_type='73/47001/47240/47241/47252'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160347' where f_post_type='73/47001/47240/47241/47257'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160258/160332/160348' where f_post_type='73/47001/47240/47241/47253'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160350/160351' where f_post_type='73/47001/47319/47320/47321'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160350/160352' where f_post_type='73/47001/47319/47320/47325'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160350/160353' where f_post_type='73/47001/47319/47320/47326'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160350/160354' where f_post_type='73/47001/47319/47320/47324'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160350/160355' where f_post_type='73/47001/47319/47320/47322'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160357' where f_post_type='73/47001/47319/47327/47329'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160358' where f_post_type='73/47001/47319/47327/47333'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160359' where f_post_type='73/47001/47319/47327/47335'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160360' where f_post_type='73/47001/47319/47327/47336'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160361' where f_post_type='73/47001/47319/47327/47337'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160362' where f_post_type='73/47001/47319/47327/47331'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160363' where f_post_type='73/47001/47319/47327/47332'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160365' where f_post_type='73/47001/47319/47327/47339'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160366' where f_post_type='73/47001/47319/47327/47328'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160367' where f_post_type='73/47001/47319/47327/47334'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160356/160368' where f_post_type='73/47001/47319/47327/47338'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160370' where f_post_type='73/47001/47319/47340/47342'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160371' where f_post_type='73/47001/47319/47340/47344'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160372' where f_post_type='73/47001/47319/47340/47347'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160373' where f_post_type='73/47001/47319/47340/47341'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160374' where f_post_type='73/47001/47319/47340/47345'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160375' where f_post_type='73/47001/47319/47340/47351'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160376' where f_post_type='73/47001/47319/47340/47343'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160377' where f_post_type='73/47001/47319/47340/47350'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160378' where f_post_type='73/47001/47319/47340/47349'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160379' where f_post_type='73/47001/47319/47340/47346'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160369/160380' where f_post_type='73/47001/47319/47340/47348'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160382' where f_post_type='73/47001/47319/47352/47355'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160383' where f_post_type='73/47001/47319/47352/47360'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160384' where f_post_type='73/47001/47319/47352/47353'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160385' where f_post_type='73/47001/47319/47352/47354'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160386' where f_post_type='73/47001/47319/47352/47357'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160387' where f_post_type='73/47001/47319/47352/47358'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160349/160381/160388' where f_post_type='73/47001/47319/47352/47359'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160390/160391' where f_post_type='73/47001/47432/47441/47442'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160393' where f_post_type='73/47001/47432/47433/47436'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160394' where f_post_type='73/47001/47432/47433/47437'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160395' where f_post_type='73/47001/47432/47433/47440'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160396' where f_post_type='73/47001/47432/47433/47435'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160397' where f_post_type='73/47001/47432/47433/47439'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160398' where f_post_type='73/47001/47432/47433/47434'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160392/160399' where f_post_type='73/47001/47432/47433/47438'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160400/160401' where f_post_type='73/47001/47258/47259/47274'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160400/160402' where f_post_type='73/47001/47258/47259/47273'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160400/160403' where f_post_type='73/47001/47258/47259/47275'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160400/160404' where f_post_type='73/47001/47258/47259/47276'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160400/160405' where f_post_type='73/47001/47002/47003/47010'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160407' where f_post_type='73/47001/47258/47259/47277'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160408' where f_post_type='73/47001/47258/47259/47267'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160409' where f_post_type='73/47001/47258/47259/47266'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160410' where f_post_type='73/47001/47258/47259/47269'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160411' where f_post_type='73/47001/47258/47259/47279'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160412' where f_post_type='73/47001/47258/47259/47263'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160413' where f_post_type='73/47001/47258/47259/47261'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160414' where f_post_type='73/47001/47258/47259/47262'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160415' where f_post_type='73/47001/47258/47259/47260'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160416' where f_post_type='73/47001/47258/47259/47268'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160417' where f_post_type='73/47001/47258/47259/47264'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160418' where f_post_type='73/47001/47258/47259/47270'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160419' where f_post_type='73/47001/47258/47259/47272'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160420' where f_post_type='73/47001/47258/47259/47280'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160389/160406/160421' where f_post_type='73/47001/47258/47259/47265'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160424' where f_post_type='73/47001/47069/47096/47097'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160425' where f_post_type='73/47001/47069/47096/47098'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160426' where f_post_type='73/47001/47069/47096/47099'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160427' where f_post_type='73/47001/47069/47096/47105'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160428' where f_post_type='73/47001/47069/47096/47100'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160429' where f_post_type='73/47001/47069/47096/47101'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160430' where f_post_type='73/47001/47069/47096/47102'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160431' where f_post_type='73/47001/47069/47096/47103'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160423/160432' where f_post_type='73/47001/47069/47096/47104'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160434' where f_post_type='73/47001/47069/47070/47074'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160435' where f_post_type='73/47001/47069/47070/47076'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160436' where f_post_type='73/47001/47069/47070/47080'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160437' where f_post_type='73/47001/47069/47070/47079'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160438' where f_post_type='73/47001/47069/47070/47071'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160439' where f_post_type='73/47001/47069/47070/47073'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160440' where f_post_type='73/47001/47069/47070/47083'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160441' where f_post_type='73/47001/47069/47070/47077'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160442' where f_post_type='73/47001/47069/47070/47084'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160443' where f_post_type='73/47001/47069/47070/47082'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160444' where f_post_type='73/47001/47069/47070/47081'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160445' where f_post_type='73/47001/47069/47070/47085'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160446' where f_post_type='73/47001/47069/47070/47072'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160447' where f_post_type='73/47001/47069/47070/47086'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160448' where f_post_type='73/47001/47069/47070/47078'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160433/160449' where f_post_type='73/47001/47069/47070/47075'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160451' where f_post_type='73/47001/47069/47087/47090'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160452' where f_post_type='73/47001/47069/47087/47088'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160453' where f_post_type='73/47001/47069/47087/47091'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160454' where f_post_type='73/47001/47069/47087/47092'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160455' where f_post_type='73/47001/47069/47087/47089'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160456' where f_post_type='73/47001/47069/47087/47094'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160422/160450/160457' where f_post_type='73/47001/47069/47087/47095'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160460' where f_post_type='73/47001/47144/47158/47164'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160461' where f_post_type='73/47001/47144/47158/47166'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160462' where f_post_type='73/47001/47144/47158/47173'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160463' where f_post_type='73/47001/47144/47158/47171'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160464' where f_post_type='73/47001/47144/47158/47174'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160465' where f_post_type='73/47001/47144/47158/47165'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160466' where f_post_type='73/47001/47144/47158/47169'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160467' where f_post_type='73/47001/47144/47158/47167'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160468' where f_post_type='73/47001/47144/47158/47172'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160469' where f_post_type='73/47001/47144/47158/47163'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160459/160470' where f_post_type='73/47001/47144/47158/47170'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160472' where f_post_type='73/47001/47144/47145/47150'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160473' where f_post_type='73/47001/47144/47145/47149'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160474' where f_post_type='73/47001/47144/47145/47152'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160475' where f_post_type='73/47001/47144/47145/47148'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160476' where f_post_type='73/47001/47144/47145/47146'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160477' where f_post_type='73/47001/47144/47145/47151'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160478' where f_post_type='73/47001/47144/47145/47154'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160479' where f_post_type='73/47001/47144/47145/47156'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160480' where f_post_type='73/47001/47144/47145/47147'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160481' where f_post_type='73/47001/47144/47145/47153'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160482' where f_post_type='73/47001/47144/47145/47155'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160471/160483' where f_post_type='73/47001/47144/47145/47157'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160486' where f_post_type='73/47001/47144/47158/47175'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160487' where f_post_type='73/47001/47144/47158/47159'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160488' where f_post_type='73/47001/47144/47158/47160'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160491' where f_post_type='73/47001/47144/47158/47161'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160492' where f_post_type='73/47001/47144/47158/47168'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160493' where f_post_type='73/47001/47144/47158/47162'").execute();
		IdeaJdbc.sql("update t_post_channel set f_post_type ='73/47001/160458/160484/160495' where f_post_type='73/47001/47144/47158/47176'").execute();
	}

}
