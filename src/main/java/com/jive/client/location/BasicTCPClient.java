package com.jive.client.location;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Basic TCP client to test the server
 */

public class BasicTCPClient {

	public BasicTCPClient() {
	}

	public static void main(String argv[]) throws Exception {
		int serverPort = 5555;
		String ip = "localhost";
		String data = "0155eaf81e9b6389e2000100620009";
		try (Socket socket = new Socket(ip, serverPort);
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());) {
			// sendAndCollect("0155eaf81e9b6389e2000100620009", output, input);
			sendAndCollect("01552608338f396a57000100610001", output, input);
			// sendAndCollect("0155eaf81e9b6389e2000100620009\r\n01552608338f396a57000100610001",
			// output, input);
			// sendAndCollect("0155eaf81e9b6389e2000100620009\n01552608338f396a57000100610001",
			// output, input);
		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		}
	}

	public static void sendAndCollect(String data, DataOutputStream output, DataInputStream input) throws Exception {
		output.writeBytes(data);
		StringBuffer inputLine = new StringBuffer();
		String tmp;
		while ((tmp = input.readLine()) != null) {
			inputLine.append(tmp);
			System.out.println(tmp);
		}
	}

}
