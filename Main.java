import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Interfaz para los tickets
interface Ticket {
    double calcularIngreso();

    String getPrioridad();
}

// Clase para el Ticket de Prioridad Alta
class TicketAlta implements Ticket {
    private double totalHoras;
    private int comision;

    public TicketAlta(double totalHoras, int comision) {
        this.totalHoras = totalHoras;
        this.comision = comision;
    }

    @Override
    public double calcularIngreso() {
        double ingreso = totalHoras * 8;
        if (comision >= 1 && comision <= 5) {
            ingreso += ingreso * 0.05; // 5% de comisión para días laborables
        } else {
            ingreso += ingreso * 0.2; // 20% de comisión para fines de semana
        }
        return ingreso;
    }

    @Override
    public String getPrioridad() {
        return "Alta";
    }

    public int getComision() {
        return comision;
    }
}

// Clase para el Ticket de Prioridad Media
class TicketMedia implements Ticket {
    private double totalHoras;
    private int comisionEmpleado;

    public TicketMedia(double totalHoras, int comisionEmpleado) {
        this.totalHoras = totalHoras;
        this.comisionEmpleado = comisionEmpleado;
    }

    @Override
    public double calcularIngreso() {
        double ingreso = totalHoras * 10;
        if (comisionEmpleado >= 5 && comisionEmpleado <= 10) {
            ingreso += ingreso * (comisionEmpleado / 100.0); // Comisión en base al porcentaje de empleados
        }
        return ingreso;
    }

    @Override
    public String getPrioridad() {
        return "Media";
    }

    public int getComisionEmpleado() {
        return comisionEmpleado;
    }
}

// Clase para el Ticket de Prioridad Baja
class TicketBaja implements Ticket {
    private double totalHoras;

    public TicketBaja(double totalHoras) {
        this.totalHoras = totalHoras;
    }

    @Override
    public double calcularIngreso() {
        return totalHoras * 10;
    }

    @Override
    public String getPrioridad() {
        return "Baja";
    }

    public double getTotalHoras() {
        return totalHoras;
    }
}

// Clase tickets
class TicketManager {
    private List<Ticket> tickets = new ArrayList<>();

    public void agregarTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void generarReporteIngresos() {
        double ingresoAlta = 0;
        double ingresoMedia = 0;
        double ingresoBaja = 0;

        for (Ticket ticket : tickets) {
            if (ticket instanceof TicketAlta) {
                ingresoAlta += ticket.calcularIngreso();
            } else if (ticket instanceof TicketMedia) {
                ingresoMedia += ticket.calcularIngreso();
            } else if (ticket instanceof TicketBaja) {
                ingresoBaja += ticket.calcularIngreso();
            }
        }

        System.out.println("Ingresos por prioridad:");
        System.out.println("Prioridad Alta: $" + ingresoAlta);
        System.out.println("Prioridad Media: $" + ingresoMedia);
        System.out.println("Prioridad Baja: $" + ingresoBaja);

        if (ingresoAlta > ingresoMedia && ingresoAlta > ingresoBaja) {
            System.out.println("La prioridad Alta genera el mayor ingreso.");
        } else if (ingresoMedia > ingresoAlta && ingresoMedia > ingresoBaja) {
            System.out.println("La prioridad Media genera el mayor ingreso.");
        } else {
            System.out.println("La prioridad Baja genera el mayor ingreso.");
        }
    }

    public void guardarTicketsEnCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (Ticket ticket : tickets) {
                if (ticket instanceof TicketAlta) {
                    writer.println("Alta," + ((TicketAlta) ticket).calcularIngreso() + "," + ((TicketAlta) ticket).getComision());
                } else if (ticket instanceof TicketMedia) {
                    writer.println("Media," + ((TicketMedia) ticket).calcularIngreso() + "," + ((TicketMedia) ticket).getComisionEmpleado());
                } else if (ticket instanceof TicketBaja) {
                    writer.println("Baja," + ((TicketBaja) ticket).calcularIngreso() + "," + ((TicketBaja) ticket).getTotalHoras());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cargarTicketsDesdeCSV(String filename) {
        tickets.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String prioridad = parts[0];
                    double ingreso = Double.parseDouble(parts[1]);
                    switch (prioridad) {
                        case "Alta":
                            int comision = Integer.parseInt(parts[2]);
                            tickets.add(new TicketAlta(0, comision)); // Nota: no se almacena total de horas para cargar desde CSV
                            break;
                        case "Media":
                            int comisionEmpleado = Integer.parseInt(parts[2]);
                            tickets.add(new TicketMedia(0, comisionEmpleado)); // Nota: no se almacena total de horas para cargar desde CSV
                            break;
                        case "Baja":
                            double totalHoras = Double.parseDouble(parts[2]);
                            tickets.add(new TicketBaja(totalHoras)); // Nota: no se almacena comisión para cargar desde CSV
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void agregarTicketDesdeConsola() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la prioridad del ticket (Alta, Media o Baja):");
        String prioridad = scanner.nextLine();
        System.out.println("Ingrese el total de horas trabajadas:");
        double totalHoras = scanner.nextDouble();
        scanner.nextLine(); // Consumir el salto de línea
        if (prioridad.equalsIgnoreCase("Alta")) {
            System.out.println("Ingrese la comisión (1-10):");
            int comision = scanner.nextInt();
            agregarTicket(new TicketAlta(totalHoras, comision));
        } else if (prioridad.equalsIgnoreCase("Media")) {
            System.out.println("Ingrese el porcentaje de comisión para los empleados (5-10):");
            int comisionEmpleado = scanner.nextInt();
            agregarTicket(new TicketMedia(totalHoras, comisionEmpleado));
        } else if (prioridad.equalsIgnoreCase("Baja")) {
            agregarTicket(new TicketBaja(totalHoras));
        } else {
            System.out.println("Prioridad inválida.");
        }
    }
}
public class Main {
    public static void main(String[] args) {
        TicketManager ticketManager = new TicketManager();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        while (!salir) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Agregar ticket");
            System.out.println("2. Generar reporte de ingresos");
            System.out.println("3. Guardar tickets en archivo CSV");
            System.out.println("4. Cargar tickets desde archivo CSV");
            System.out.println("5. Salir");
            int opcion = 0;
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea
                switch (opcion) {
                    case 1:
                        ticketManager.agregarTicketDesdeConsola();
                        break;
                    case 2:
                        ticketManager.generarReporteIngresos();
                        break;
                    case 3:
                        System.out.println("Ingrese el nombre del archivo CSV:");
                        String filenameGuardar = scanner.nextLine();
                        ticketManager.guardarTicketsEnCSV(filenameGuardar);
                        break;
                    case 4:
                        System.out.println("Ingrese el nombre del archivo CSV:");
                        String filenameCargar = scanner.nextLine();
                        ticketManager.cargarTicketsDesdeCSV(filenameCargar);
                        break;
                    case 5:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                        break;
                }
            } else {
                break;
            }
        }
        scanner.close(); // Cerrar el Scanner
    }
}