import java.util.*;

public class SJFwithRR {
    public static void main(String[] args) {
        Scanner k = new Scanner(System.in);

        int q; // Quantum number

        // Arrays to store process information
        ArrayList<Integer> processID = new ArrayList<Integer>();
        ArrayList<Integer> arrivalTime = new ArrayList<Integer>();
        ArrayList<Integer> burstTime = new ArrayList<Integer>();

        // Ready queue for processes
        ArrayList<Integer> readyQueue = new ArrayList<Integer>();

        // Gantt chart to visualize the execution sequence
        Queue<Integer> ganttChartProcessList = new LinkedList<>();

        // Time line for Gantt chart
        Queue<Integer> timeLine = new LinkedList<>();

        int enteredPID, enteredAT, enteredBT; // Variables to store user input
        boolean exit = false;

        // User input: quantum number and process details
        System.out.println("Enter the quantum number:");
        q = k.nextInt();
        System.out.println("Enter process ID, arrival time and burst time (enter 0 0 0 to stop):");
        while (!exit) {
            enteredPID = k.nextInt();
            enteredAT = k.nextInt();
            enteredBT = k.nextInt();
            if (enteredPID == 0 && enteredAT == 0 && enteredBT == 0) {
                exit = true; // Exit loop if user inputs 0 0 0
            } else {
                // Store process information
                processID.add(enteredPID);
                arrivalTime.add(enteredAT);
                burstTime.add(enteredBT);
            }
        }

        // Sort processes by arrival time
        int n = arrivalTime.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arrivalTime.get(j) > arrivalTime.get(j + 1)) {
                    int temp1 = processID.get(j);
                    int temp2 = arrivalTime.get(j);
                    int temp3 = burstTime.get(j);
                    // sort process id
                    processID.set(j, processID.get(j + 1));
                    processID.set(j + 1, temp1);

                    // sort process Arrival Time
                    arrivalTime.set(j, arrivalTime.get(j + 1));
                    arrivalTime.set(j + 1, temp2);

                    // sort process burst time
                    burstTime.set(j, burstTime.get(j + 1));
                    burstTime.set(j + 1, temp3);

                }

            }
        }

        int[] start = new int[arrivalTime.size()];
        int[] finish = new int[arrivalTime.size()];

        // Copy process information arrays
        int[] arrivalTimeCopy = new int[arrivalTime.size()];
        int[] burstTimeCopy = new int[burstTime.size()];
        int[] processIDCopy = new int[processID.size()];
        for (int i = 0; i < arrivalTime.size(); i++) {
            arrivalTimeCopy[i] = arrivalTime.get(i);
            burstTimeCopy[i] = burstTime.get(i);
            processIDCopy[i] = processID.get(i);
        }

        int time = 0; // Current time
        int currentRunningProcess = 0; // Current running process ID
        int index = 0; // Index of current running process in arrays

        // Initialize time line and ready queue with first process
        int minBT = 0;
        int minBtIndex = 0;
        if (arrivalTime.get(0) < arrivalTime.get(1)) {
            timeLine.add(arrivalTime.get(0));
            readyQueue.add(processID.get(0));
            start[0] = arrivalTime.get(0);
        } else {
            minBT = burstTime.get(minBtIndex);
            for (int i = 0; i < arrivalTime.size(); i++) {
                if (i == (arrivalTime.size() - 1)) {
                    if (burstTime.get(i) < minBT) {
                        minBT = burstTime.get(i);
                        minBtIndex = i;
                    }
                    break;
                }
                if (arrivalTime.get(i) < arrivalTime.get(i + 1)) {
                    if (burstTime.get(i) < minBT) {
                        minBT = burstTime.get(i);
                        minBtIndex = i;
                    }
                    break;
                }

                if (burstTime.get(i) < minBT) {
                    minBT = burstTime.get(i);
                    minBtIndex = i;
                }
            }
        }
        if (minBT != 0) {
            timeLine.add(arrivalTime.get(minBtIndex));
            readyQueue.add(processID.get(minBtIndex));
            start[minBtIndex] = arrivalTime.get(minBtIndex);
        }

        // Execute processes until ready queue is empty
        while (!readyQueue.isEmpty()) {
            currentRunningProcess = readyQueue.remove(0); // Get the next process to execute
            index = processID.indexOf(currentRunningProcess); // Get index of current process
            if (arrivalTime.get(index) <= time) {
                // If process has arrived, execute it
                if (!ganttChartProcessList.contains(processID.get(index)))
                    start[index] = time; // Record start time if process is starting execution
                if (burstTime.get(index) >= q) {
                    // If burst time is greater than quantum
                    ganttChartProcessList.add(processID.get(index));
                    burstTime.set(index, burstTime.get(index) - q);
                    time += q;
                    timeLine.add(time);
                    if (burstTime.get(index) == 0)
                        finish[index] = time; // Record finish time if process is completed
                } else if (burstTime.get(index) < q && burstTime.get(index) != 0) {
                    // If burst time is less than quantum
                    ganttChartProcessList.add(processID.get(index));
                    time += burstTime.get(index);
                    timeLine.add(time);
                    burstTime.set(index, 0);
                    finish[index] = time;
                }

                ArrayList<Integer> currentreadyProcessBursts = new ArrayList<Integer>();
                ArrayList<Integer> currentreadyProcessPID = new ArrayList<Integer>();
                for (int i = 0; i < arrivalTime.size(); i++) {
                    if (arrivalTime.get(i) <= time) {
                        if (burstTime.get(i) != 0) {
                            currentreadyProcessBursts.add(burstTime.get(i));
                            currentreadyProcessPID.add(processID.get(i));
                        }
                    }
                }
                if (currentreadyProcessPID.size() != 0) {
                    for (int i = 0; i < currentreadyProcessPID.size() - 1; i++) {
                        for (int j = 0; j < currentreadyProcessPID.size() - 1 - i; j++) {
                            if (currentreadyProcessBursts.get(j) > currentreadyProcessBursts.get(j + 1)) {
                                int temp1 = currentreadyProcessPID.get(j);
                                int temp2 = currentreadyProcessBursts.get(j);
                                // sort currentreadyProcessPid
                                currentreadyProcessPID.set(j, currentreadyProcessPID.get(j + 1));
                                currentreadyProcessPID.set(j + 1, temp1);

                                // sort currentreadyProcessBursts
                                currentreadyProcessBursts.set(j, currentreadyProcessBursts.get(j + 1));
                                currentreadyProcessBursts.set(j + 1, temp2);
                            }
                        }
                    }
                    readyQueue.clear();
                    for (int i = 0; i < currentreadyProcessPID.size(); i++) {
                        readyQueue.add(currentreadyProcessPID.get(i));
                    }
                }

            } else if (arrivalTime.get(index) > time) {
                // If no process has arrived, move to the next time unit
                time++;
                if (readyQueue.isEmpty())
                    readyQueue.add(processID.get(index));
            }

        }

        // Print Gantt chart
        System.out.println();
        System.out.println("SJF with Round Robin (RR) scheduling algorithm: ");
        System.out.println("Gantt chart: ");
        while (!timeLine.isEmpty()) {
            System.out.print(timeLine.remove() + " | p" + ganttChartProcessList.remove() + " | ");
            if (ganttChartProcessList.isEmpty())
                System.out.print(timeLine.remove());
        }

        // Calculate and print average turnaround, response, and waiting times
        System.out.println();
        System.out.println();
        int waitingTime, turnAroundTime, responseTime;
        double totalWaitingTime = 0, totalTurnAroundTime = 0, totalResponseTime = 0;

        for (int i = 0; i < arrivalTime.size(); i++) {
            // Calculate turnaround, response, and waiting times for each process
            responseTime = start[i] - arrivalTimeCopy[i];
            totalResponseTime += responseTime;
            turnAroundTime = finish[i] - arrivalTimeCopy[i];
            totalTurnAroundTime += turnAroundTime;
            waitingTime = turnAroundTime - burstTimeCopy[i];
            totalWaitingTime += waitingTime;
            System.out.println("P" + processIDCopy[i] + ":");
            System.out.println("--------------------");
            System.out.println("Turnaround Time = " + turnAroundTime);
            System.out.println("Response Time = " + responseTime);
            System.out.println("Waiting Time = " + waitingTime);
            System.out.println("--------------------");
            System.out.println();

        }
        System.out.println();
        System.out.println("Average Time: ");
        System.out.println("Average Turnaround time: " + totalTurnAroundTime / n);
        System.out.println("Average Response time: " + totalResponseTime / n);
        System.out.println("Average Waiting time: " + totalWaitingTime / n);

    }
}
