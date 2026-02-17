/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;

import util.DateUtil;
import app.AppException;
import app.checkin.CheckInRoomForm;
import app.checkout.CheckOutRoomForm;
import app.reservation.ReserveRoomForm;

/**
 * CUI class for Hotel Reservation Systems
 * 
 */
public class CUI {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private BufferedReader reader;

	CUI() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void execute() throws IOException {
		try {
			while (true) {
				int selectMenu;
				System.out.println("");
				System.out.println("Menu");
				System.out.println("1: Reservation");
				System.out.println("2: Check-in");
				System.out.println("3: Check-out");
				System.out.println("4: Cancel-Reservation");
				System.out.println("9: End");
				System.out.print("> ");

				try {
					String menu = reader.readLine();
					selectMenu = Integer.parseInt(menu);
				}
				catch (NumberFormatException e) {
					selectMenu = 5;
				}

				if (selectMenu == 9) {
					break;
				}

				switch (selectMenu) {
					case 1:
						reserveRoom();
						break;
					case 2:
						checkInRoom();
						break;
					case 3:
						checkOutRoom();
						break;
					case 4:
						cancelReservation();
				}
			}
			System.out.println("Ended");
		}
		catch (AppException e) {
			System.err.println("Error");
			System.err.println(e.getFormattedDetailMessages(LINE_SEPARATOR));
		}
		finally {
			reader.close();
		}
	}
	
	private int duration = 0;

	private void reserveRoom() throws IOException, AppException {
		Scanner scanner = new Scanner(System.in);

        System.out.println("Input Your Name:");
        System.out.print("> ");
        String name = scanner.nextLine(); 
        System.out.println("Input arrival date in the form of yyyy/mm/dd");
        System.out.print("> ");
        String dateStr = scanner.nextLine();

        System.out.println("Input the duration of stay (no. of days)");
        System.out.print("> ");
        duration = scanner.nextInt();

        scanner.nextLine(); 

        System.out.println("Input the type of Room (Standard / Deluxe / Suite / Family)");
        System.out.print("> ");
        String roomType = scanner.nextLine();

		// Validate input
		Date stayingDate = DateUtil.convertToDate(dateStr);
		if (stayingDate == null) {
			System.out.println("Invalid input");
			return;
		}

		ReserveRoomForm reserveRoomForm = new ReserveRoomForm();
		reserveRoomForm.setStayingDate(stayingDate);
		String reservationNumber = reserveRoomForm.submitReservation();
		System.out.println();
		System.out.println();
		System.out.println("<Booking Summary>");
		System.out.println();
		System.out.println();
        System.out.println("Reservation has been completed.");
		System.out.println();
        System.out.println("Name: " + name);
		System.out.println();
        System.out.println("Arrival (staying) date is " + DateUtil.convertToString(stayingDate) + ".");
		System.out.println();
        System.out.println("Duration of stay is " + duration + " day(s)");
		System.out.println();
        System.out.println("Room Type: " + roomType);
		System.out.println();
        System.out.println("Reservation number is " + reservationNumber + ".");
	}
	
	private void cancelReservation() throws IOException, AppException {

		System.out.println("Input reservation number");
		System.out.print("> ");

		String reservationNumber = reader.readLine();

		if (reservationNumber == null || reservationNumber.length() == 0) {
			System.out.println("Invalid reservation number");
			return;
		}
		ReserveRoomForm reserveRoomForm = new ReserveRoomForm();
		reserveRoomForm.setReservationNumber(reservationNumber);
		reserveRoomForm.cancelReservation();
		System.out.println("Cancellation has been completed.");
	}

	private void checkInRoom() throws IOException, AppException {
		System.out.println("Input reservation number");
		System.out.print("> ");

		String reservationNumber = reader.readLine();

		if (reservationNumber == null || reservationNumber.length() == 0) {
			System.out.println("Invalid reservation number");
			return;
		}

		CheckInRoomForm checkInRoomForm = new CheckInRoomForm();
		checkInRoomForm.setReservationNumber(reservationNumber);

		String roomNumber = checkInRoomForm.checkIn();
		System.out.println("Check-in has been completed.");
		System.out.println("Room number is " + roomNumber + ".");

	}

	private void checkOutRoom() throws IOException, AppException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input room number");
		System.out.print("> ");

		String roomNumber = reader.readLine();

		if (roomNumber == null || roomNumber.length() == 0) {
			System.out.println("Invalid room number");
			return;
		}

		int totalAmount = 8000 * duration;
		int amountPaid = 0;
	
		while (amountPaid < totalAmount) {
			System.out.println("Amount to pay: " + (totalAmount - amountPaid));
			System.out.print("> ");
	
			try {
				int payment = scanner.nextInt();
				if (payment <= 0) {
					System.out.println("Invalid payment amount. Please enter a positive value.");
					continue;
				}
				amountPaid += payment;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			}
		}
	
		System.out.println("Payment completed. Total paid: " + amountPaid);

		CheckOutRoomForm checkoutRoomForm = new CheckOutRoomForm();
		checkoutRoomForm.setRoomNumber(roomNumber);
		checkoutRoomForm.checkOut();
		System.out.println("Check-out has been completed.");
	}

	public static void main(String[] args) throws Exception {
		CUI cui = new CUI();
		cui.execute();
	}
}
