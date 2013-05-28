/*   
    Copyright (C) 2013 Mario Krumnow, Dresden University of Technology

    This file is part of TraaS.

    TraaS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License.

    TraaS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TraaS.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.tudresden.sumo.util;

import java.util.LinkedList;

import de.tudresden.sumo.config.Constants;
import de.tudresden.ws.container.SumoColor;
import de.tudresden.ws.container.SumoGeometry;
import de.tudresden.ws.container.SumoPosition2D;
import de.tudresden.ws.container.SumoTLSLogic;

import it.polito.appeal.traci.protocol.Command;

public class SumoCommand {
	
	Command cmd;
    public LinkedList<Object> raw;
	public int input1;
	public int input2;
	String input3;
	
	int response;
	int output_type;

	//Get Statements
	public SumoCommand(Object input1, Object input2, Object input3, Object response, Object output_type){
	
		this.cmd = new Command((Integer) input1);
		cmd.content().writeUnsignedByte((Integer) input2);
		cmd.content().writeStringASCII(String.valueOf(input3));
		
		this.input1=(Integer) input1;
		this.input2=(Integer) input2;
		this.input3=String.valueOf(input3);
		
		this.response = (Integer) response;
		this.output_type = (Integer) output_type;
		
		this.raw = new LinkedList<Object>();
		this.raw.add(input1);
		this.raw.add(input2);
		this.raw.add(input3);
		this.raw.add(response);
		this.raw.add(output_type);
		
	}

	public SumoCommand(Object input1, Object input2, Object input3, Object[] array, Object response, Object output_type){
		
		this.cmd = new Command((Integer) input1);
		cmd.content().writeUnsignedByte((Integer) input2);
		cmd.content().writeStringASCII(String.valueOf(input3));
		
		cmd.content().writeUnsignedByte(Constants.TYPE_COMPOUND);
		cmd.content().writeInt(array.length);	
		for(int i=0; i<array.length; i++){
			add_type(array[i]);
			add_variable(array[i]);
		}
		
		this.input1=(Integer) input1;
		this.input2=(Integer) input2;
		this.input3=String.valueOf(input3);
		
		this.response = (Integer) response;
		this.output_type = (Integer) output_type;
		
		this.raw = new LinkedList<Object>();
		this.raw.add(input1);
		this.raw.add(input2);
		this.raw.add(input3);
		this.raw.add(response);
		this.raw.add(output_type);
		
	}
	
	public SumoCommand(Object input1, Object input3){
		
		this.input1=(Integer) input1;
		this.input2=(Integer) input3;
		
		this.cmd = new Command((Integer) input1);
		this.add_variable(input3);
		
		this.raw = new LinkedList<Object>();
		this.raw.add(input1);
		this.raw.add(input3);
		
	}

	public SumoCommand(Object input1, Object input2, Object input3, Object[] array){
		
		this.cmd = new Command((Integer) input1);
		this.input1=(Integer) input1;
		this.input2=(Integer) input2;
		
		cmd.content().writeUnsignedByte((Integer) input2);
		cmd.content().writeStringASCII(String.valueOf(input3));
		
		if((Integer) input2 == Constants.VAR_COLOR){
			cmd.content().writeUnsignedByte(Constants.TYPE_COLOR);
			for(int i=0; i<array.length; i++){
				add_variable(array[i]);
			}
		}
		else if((Integer) input2 == Constants.VAR_VIEW_BOUNDARY){
				cmd.content().writeUnsignedByte(Constants.TYPE_BOUNDINGBOX);
				for(int i=0; i<array.length; i++){
					add_variable(array[i]);
				}	
				
		}else if((Integer) input2 == Constants.VAR_VIEW_OFFSET){
		
			cmd.content().writeUnsignedByte(Constants.POSITION_2D);
			for(int i=0; i<array.length; i++){
				add_variable(array[i]);
			}	
			
		}else if((Integer) input1 == Constants.CMD_SET_POLYGON_VARIABLE && (Integer) input2 == Constants.ADD){
			
			
			cmd.content().writeUnsignedByte(Constants.TYPE_COMPOUND);
			SumoGeometry ss = (SumoGeometry) array[0];
			cmd.content().writeInt(ss.coords.size());	
			
			add_type(array[3]);
			add_variable(array[3]);
	
			//color
			add_type(array[1]);
			add_variable(array[1]);
			
			//fill
			add_type(array[2]);
			add_variable(array[2]);
			
			//layer
			add_type(array[4]);
			add_variable(array[4]);
			
			//shape
			add_type(array[0]);
			add_variable(array[0]);
			
		}
		
		else{
			cmd.content().writeUnsignedByte(Constants.TYPE_COMPOUND);
			cmd.content().writeInt(array.length);	
			for(int i=0; i<array.length; i++){
				add_type(array[i]);
				add_variable(array[i]);
			}
		}
		
		this.raw = new LinkedList<Object>();
		this.raw.add(input1);
		this.raw.add(input2);
		this.raw.add(input3);
		this.raw.add(array);
		
		
	}

	public SumoCommand(Object input1, Object input2, Object input3, Object input){
		
		this.cmd = new Command((Integer) input1);
		this.input1=(Integer) input1;
		this.input2=(Integer) input2;
		
		cmd.content().writeUnsignedByte((Integer) input2);
		cmd.content().writeStringASCII(String.valueOf(input3));
		
		if(input.getClass().equals(StringList.class)){
		
			StringList sl = (StringList) input;
			cmd.content().writeUnsignedByte(Constants.TYPE_STRINGLIST);
			cmd.content().writeInt(sl.size());
			
			for(int i=0; i<sl.size(); i++){
				cmd.content().writeStringASCII(sl.get(i));
			}
			
		}else if(input.getClass().equals(SumoTLSLogic.class)){
		
			@SuppressWarnings("unused")
			SumoTLSLogic stl = (SumoTLSLogic) input;
		
		
		}else if(input.getClass().equals(SumoGeometry.class)){

			
		}
		else{
			add_type(input);
			add_variable(input);
		}
		
		this.raw = new LinkedList<Object>();
		this.raw.add(input1);
		this.raw.add(input2);
		this.raw.add(input3);
		this.raw.add(input);
		
	}
	
	
	public Object[] get_raw(){
		
		Object[] output = new Object[this.raw.size()];
		for(int i=0; i<this.raw.size(); i++){
			output[i] = this.raw.get(i);
		}
		
		return output;
	}
	
	
	private void add_type(Object input){
		
		if(input.getClass().equals(Integer.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_INTEGER);
		}else if(input.getClass().equals(String.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_STRING);
		}else if(input.getClass().equals(Double.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_DOUBLE);
		}else if(input.getClass().equals(Byte.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_BYTE);
		}else if(input.getClass().equals(SumoColor.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_COLOR);
		}else if(input.getClass().equals(SumoGeometry.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_POLYGON);
		}else if(input.getClass().equals(Boolean.class)){
			this.cmd.content().writeUnsignedByte(Constants.TYPE_UBYTE);
		}
		
	}

	private void add_variable(Object input){
		
		if(input.getClass().equals(Integer.class)){
			this.cmd.content().writeInt((Integer) input);
		}else if(input.getClass().equals(String.class)){
			this.cmd.content().writeStringASCII((String) input);
		}else if(input.getClass().equals(Double.class)){
			this.cmd.content().writeDouble((Double) input);
		}else if(input.getClass().equals(Byte.class)){
			this.cmd.content().writeByte((Byte) input);
		}else if(input.getClass().equals(Boolean.class)){
			boolean b = (Boolean) input;
			cmd.content().writeUnsignedByte(b ? 1 : 0);
		}
		else if(input.getClass().equals(SumoColor.class)){
		
			SumoColor sc = (SumoColor) input;
			this.cmd.content().writeByte(sc.r);
			this.cmd.content().writeByte(sc.g);
			this.cmd.content().writeByte(sc.b);
			this.cmd.content().writeByte(sc.a);
			
		}else if(input.getClass().equals(SumoGeometry.class)){
			
			SumoGeometry sg = (SumoGeometry) input;
			cmd.content().writeUnsignedByte(sg.coords.size());
			
			for(SumoPosition2D pos : sg.coords){		
				cmd.content().writeDouble(pos.x);
				cmd.content().writeDouble(pos.y);
			}
			
			
		}
		
	}

	public Command get_command(){
		return this.cmd;
	}
	
}
