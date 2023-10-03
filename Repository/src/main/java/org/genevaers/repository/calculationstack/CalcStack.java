package org.genevaers.repository.calculationstack;

/*
 * Copyright Contributors to the GenevaERS Project. SPDX-License-Identifier: Apache-2.0 (c) Copyright IBM Corporation 2008
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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CalcStack {
    private int bufferLength;
    private ByteBuffer buffer;
    private int columnNumber;
    private int columnID;
    private List<CalcStackEntry> stackEntries = new ArrayList<>();

    public static String spaces = StringUtils.repeat(" ", 256);

    public enum CalcStackOpcode {
        CalcStackOpcodeInvalid("Invalid"),
        // All branches followed by unsigned long
        // All branch values are forward relative offsets (from the beginning of
        // the calculation buffer)
        // Branch to 0xffffffff means to do SKIPIF action
        CalcStackBranchAlways("Branch"), // Unconditional, no elements popped
        CalcStackBranchEQ("Branch EQ"), // Pops 2 elements, compares, branches if equal
        CalcStackBranchNE("Branch NE"), // These branches never push anything
        CalcStackBranchGT("Branch GT"),
        CalcStackBranchGE("Branch GE"),
        CalcStackBranchLT("Branch LT"),
        CalcStackBranchLE("Branch LE"),

        CalcStackPushNumber("Push Number"), // Followed by 48-byte ASCII blank filled text
        CalcStackPushColumn("Push Column"), // Followed by 4-byte unsigned long
        CalcStackAdd("Add"), // Pop 2 elements from stack, add, push result
        CalcStackSubtract("Subtract"), // Pop 2 elements from stack, subtract, push result
        CalcStackMultiply("Multiply"), // Pop 2 elements from stack, multiply, push result
        CalcStackDivide("Divide"), // Pop 2 elements from stack, divide, push result
        CalcStackNegate("Negate"), // Apply unary minus to element on top of stack
        CalcStackAbs("Abs"), // Apply absolute value to element on top of stack
        CalcStackPow("Pow"), // Followed by 4-byte unsigned long; raise the element
                      // on the top of the stack to the exponent specified
        CalcStackRound("Round"), // Followed by 4-bye unsigned long; round the
                        // element on the top of the stack to the
                        // specified number of decimal places
        CalcStackPushPriorColumn("Prior Column"), // Followed by 4-byte unsigned long
        CalcStackPushText("Push Text"), // Followed by 256-byte ASCII blank filled text
        CalcStackPushInternal("Interna;"), // Value following is the internal numeric
                               // form for this platform. It is the result
                               // of converting in-situ CalcStackPushNumber
        CalcStackEnd("End");

        private String name;

        private CalcStackOpcode(String n) {
            name = n;
        }
        @Override
        public String toString() {
            return name;
        }
    
       }

    public CalcStack(ByteBuffer buff, int i, int colID) {
        buffer = buff;
        columnNumber = i;
        columnID = colID;
    }

    /*
     * Print
     *
     * Purpose:
     * Print the entire instruction stream to the specified file
     *
     * Parameters:
     * none
     *
     * Return Value:
     * none
     */

    // void Print (FILE * pFile /* =stdout */)
    // {
    /*
     * fprintf (pFile,
     * "                           Offset  Row  Opcode       Value\n"
     * "                           -------------------------------\n");
     * 
     * int lRowNum = 0;
     * std::vector <unsigned char>::iterator it = m_CodeArray.begin();
     * while (it != m_CodeArray.end())
     * {
     * const CalcStackOpcode * pOpcode =
     * REINTERPRET_CAST(const CalcStackOpcode *, *it);
     * it += sizeof (CalcStackOpcode);
     * 
     * const int32_t * plNumber =
     * REINTERPRET_CAST(const int32_t *, *it);
     * const int * number =
     * REINTERPRET_CAST(const int *, *it);
     * fprintf (pFile, "                           %6" FMT3264 "d  %3lu  ",
     * pCurrent - sizeof (CalcStackOpcode) - m_ptr,
     * ++lRowNum);
     * switch (* pOpcode)
     * {
     * case CalcStackPushNumber :
     * {
     * const char * psz = REINTERPRET_CAST(const char *, *it);
     * CVDPShortString strTemp = psz;
     * std::string strNumber (strTemp, strTemp.getLength());
     * fprintf (pFile, "Push Number  %s\n", strNumber.c_str ());
     * pCurrent += SHORT_STRING_LEN;
     * break;
     * }
     * case CalcStackPushInternal :
     * {
     * const char * psz = REINTERPRET_CAST(const char *, *it);
     * CGenevaNum decValue;
     * decValue.SetInternData (psz);
     * const clsStrPlus & strValue = decValue.toString ();
     * fprintf (pFile, "Push Internal Number %s\n", (const char *) strValue);
     * pCurrent += SHORT_STRING_LEN;
     * break;
     * }
     * case CalcStackPushText :
     * {
     * const char * psz = REINTERPRET_CAST(const char *, *it);
     * CVDPLongString strTemp (psz);
     * std::string strText(strTemp, strTemp.getLength());
     * fprintf (pFile, "Push Text    \"%s\"\n", strText.c_str ());
     * pCurrent += LONG_STRING_LEN;
     * break;
     * }
     * case CalcStackPushColumn :
     * fprintf (pFile, "Push Column  %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchAlways :
     * fprintf (pFile, "Branch       %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchEQ :
     * fprintf (pFile, "Branch EQ    %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchNE :
     * fprintf (pFile, "Branch NE    %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchGT :
     * fprintf (pFile, "Branch GT    %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchGE :
     * fprintf (pFile, "Branch GE    %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchLT :
     * fprintf (pFile, "Branch LT    %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackBranchLE :
     * fprintf (pFile, "Branch LE    %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackAdd :
     * fprintf (pFile, "Add\n");
     * break;
     * case CalcStackSubtract :
     * fprintf (pFile, "Subtract\n");
     * break;
     * case CalcStackMultiply :
     * fprintf (pFile, "Multiply\n");
     * break;
     * case CalcStackDivide :
     * fprintf (pFile, "Divide\n");
     * break;
     * case CalcStackNegate :
     * fprintf (pFile, "Negate\n");
     * break;
     * case CalcStackAbs :
     * fprintf (pFile, "Absolute Value\n");
     * break;
     * case CalcStackPow :
     * fprintf (pFile, "Raise to     %ld\n", * plNumber);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackRound :
     * fprintf (pFile, "Round to     %ld\n", * plNumber);
     * pCurrent += sizeof (int);
     * break;
     * case CalcStackPushPriorColumn :
     * fprintf (pFile, "Push Prior Col %lu\n", * number);
     * pCurrent += sizeof (int);
     * break;
     * default :
     * fprintf (pFile, "Bad Opcode!\n");
     * break;
     * } // End of switch on Opcode
     * } // End of while loop
     * fprintf (pFile, "                           %6" FMT3264 "d  %3lu  End",
     * m_cbLen,
     * ++lRowNum);
     */
    // } // End of Print()

    public List<CalcStackEntry> buildEntriesArrayFromTheBuffer() {
        int rowNum = 0;
        //If this is from a VDP read then position is 0

        if (buffer.position() > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                int opCodeVal = buffer.getInt();
                CalcStackOpcode opCode = CalcStackOpcode.values()[opCodeVal];
                int number = 0;
                CalcStackEntry cse = null;
                switch (opCode) {
                    case CalcStackPushNumber: {
                        cse = new CalcStackShortStringEntry();
                        byte[] shortStr = new byte[48];
                        buffer.get(shortStr);
                        String numStr = new String(shortStr, 0, 48);
                        ;
                        ((CalcStackShortStringEntry) cse).setValue(numStr);
                        break;
                    }
                    case CalcStackPushInternal: {
                        // const char * psz = REINTERPRET_CAST(const char *, pCurrent);
                        // // CGenevaNum decValue;
                        // //decValue.SetInternData(psz);
                        // //const clsStrPlus & strValue = decValue.toString();
                        // std::string str = "wonky";
                        // sb.append("Push Internal Number " + str + "\n";
                        // pCurrent += SHORT_STRING_LEN;
                        break;
                    }
                    case CalcStackPushText: {
                        cse = new CalcStackLongStringEntry();
                        byte[] shortStr = new byte[256];
                        buffer.get(shortStr);
                        String text = new String(shortStr, 0, 256);
                        ;
                        ((CalcStackLongStringEntry) cse).setValue(text);
                        break;
                    }
                    case CalcStackPushColumn:
                    case CalcStackBranchAlways:
                    case CalcStackBranchEQ:
                    case CalcStackBranchNE:
                    case CalcStackBranchGT:
                    case CalcStackBranchGE:
                    case CalcStackBranchLT:
                    case CalcStackBranchLE:
                    cse = new CalcStackIntegerEntry();
                    number = buffer.getInt();
                    ((CalcStackIntegerEntry)cse).setValue(number);
                        break;
                    case CalcStackAdd:
                    case CalcStackSubtract:
                    case CalcStackMultiply:
                    case CalcStackDivide:
                    case CalcStackNegate:
                    cse = new CalcStackEntry();
                    break;
                    case CalcStackAbs:
                    case CalcStackPow:
                    case CalcStackRound:
                        // sb.append("Round to " + number + "\n";
                        // pCurrent += sizeof(int);
                        break;
                    case CalcStackPushPriorColumn:
                        // sb.append("Push Prior Col " + number + "\n";
                        // pCurrent += sizeof(int);
                        break;
                    default:
                        break;
                }
                cse.setOpCode(opCode);
                cse.setOffset(buffer.position());
                stackEntries.add(cse);
            }
        }
        return stackEntries;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of stack entries " + stackEntries.size() + "\n\n");
        sb.append(" Offset Row Opcode         Value\n");
        sb.append(" -------------------------------\n");

        int rowNum = 0;
        Iterator<CalcStackEntry> sei = stackEntries.iterator();
        while (sei.hasNext()) {
            CalcStackEntry se = sei.next();
            sb.append(String.format(" %06d %03d %-14s %s\n", se.offset, ++rowNum, se.getOpCode().toString(), se.getValue()));
        }
        CalcStackEntry last = stackEntries.get(stackEntries.size()-1);
        int endOffset = last.offset + last.length();
        sb.append(String.format(" %06d %03d %-14s %s\n", endOffset, ++rowNum, "End", ""));
        return sb.toString();
    }

    public CalcStackEntry makeAndAddCalcStackEntry(CalcStackOpcode opCode) {
        CalcStackEntry cse = new CalcStackEntry();

        switch (opCode) {
            case CalcStackPushNumber: {
                cse = new CalcStackShortStringEntry();
                break;
            }
            case CalcStackPushInternal: {
                // Not sure what this is for
                break;
            }
            case CalcStackPushText: {
                cse = new CalcStackLongStringEntry();
                break;
            }
            case CalcStackPushColumn:
                cse = new CalcStackIntegerEntry();
                break;
            case CalcStackBranchAlways:
            case CalcStackBranchEQ:
            case CalcStackBranchNE:
            case CalcStackBranchGT:
            case CalcStackBranchGE:
            case CalcStackBranchLT:
            case CalcStackBranchLE:
                cse = new CalcStackIntegerEntry();
                break;
            case CalcStackAdd:
            case CalcStackSubtract:
            case CalcStackMultiply:
            case CalcStackDivide:
            case CalcStackNegate:
            case CalcStackAbs:
                cse = new CalcStackEntry();
                break;
            case CalcStackPow:
            case CalcStackRound:
            case CalcStackPushPriorColumn:
                cse = new CalcStackIntegerEntry();
                break;
            default:
                break;
        }
        cse.setOpCode(opCode);
        stackEntries.add(cse);
        return cse;
    }

    /**
     * emitBranchXXX
     *
     * Purpose:
     * Insert a branch opcode followed by a placeholder zero target position.
     * The target position will be updated later when we know where to jump to.
     *
     * Parameters:
     * none
     *
     * Return Value:
     * position where the branch opcode is written
     * 
     * @return
     */

    public int emitBranchAlways() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchAlways);
        put(0);
        return position;
    }

    public int emitBranchEQ() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchEQ);
        put(0);
        return position;
    }

    public int emitBranchNE() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchNE);
        put(0);
        return position;
    }

    public int emitBranchGT() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchGT);
        put(0);
        return position;
    }

    public int emitBranchGE() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchGE);
        put(0);
        return position;
    }

    public int emitBranchLT() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchLT);
        put(0);
        return position;
    }

    public int emitBranchLE() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackBranchLE);
        put(0);
        return position;
    }

    public int emitPushNumber(String strNumber) {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackPushNumber);
        put(strNumber);
        return position;
    }

    public int emitPushText(String str) {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackPushText);
        putText(str);
        return position;
    }

    public int emitPushColumn(int ulNumber) {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackPushColumn);
        put(ulNumber);
        return position;
    }

    public int emitPushPrior(int ulNumber) {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackPushPriorColumn);
        put(ulNumber);
        return position;
    }

    public int emitAdd() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackAdd);
        return position;
    }

    public int emitSubtract() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackSubtract);
        return position;
    }

    public int emitMultiply() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackMultiply);
        return position;
    }

    public int emitDivide() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackDivide);
        return position;
    }

    public int emitNegate() {
        int position = buffer.position();
        put(CalcStackOpcode.CalcStackNegate);
        return position;
    }

    /**
     * putTarget
     *
     * Purpose:
     * Update the existing code array with a new forward goto target position.
     * This is use when you have to go back to fixup previously encountered
     * branch targets
     *
     * Parameters:
     * ulOpcodePos [in] The byte position where the opcode is. This is the
     * value returned by emitBranchXX
     * ulTargetPos [in] The actual forward goto position.
     *
     * Return Value:
     * none
     */

    public void putTarget(int opcodePos, int targetPos) {
        //this needs to be done to a stackEntry
        //Not the buffer
        buffer.putInt(opcodePos, targetPos);
    }

    /**
     * put
     *
     * Purpose:
     * Add a 4-byte unsigned number to the end of the code array. Note
     * that byte-swapping occurs later in the call to CSwapByteOrder
     *
     * Parameters:
     * ulNumber [in] The 4-byte value
     *
     * Return Value:
     * none
     */

    void put(int number) {
        buffer.putInt(number);
    }

    void put(CalcStackOpcode code) {
        buffer.putInt(code.ordinal());
    }

    /**
     * put
     *
     * Purpose:
     * Add a number in text form to the end of the code array.
     * Text numbers are blank padded to SHORT_STRING_LEN
     *
     * Parameters:
     * strNumber [in] String containing the number (i.e. "123.45")
     *
     * Return Value:
     * none
     */

    void put(String strNumber) {
        buffer.put(strNumber.getBytes(), 0, strNumber.length());
        buffer.put(spaces.getBytes(), 0, 48 - strNumber.length());
    }

    /**
     * put
     *
     * Purpose:
     * Add a text string to the end of the code array.
     * Text strings are blank padded to LONG_STRING_LEN. Same as the
     * routine above but with a longer length.
     *
     * Parameters:
     * strString [in] String containing the value
     *
     * Return Value:
     * none
     */

    void putText(String longString) {
        buffer.put(longString.getBytes(), 0, longString.length());
        buffer.put(spaces.getBytes(), 0, 48 - longString.length());
    }

    public short getStackLength() {
        return (short) bufferLength;
    }

    public CalcStackEntry getEntryAt(int ndx) {
        return stackEntries.get(ndx);
    }

    public void fill(List<Byte> stack) {
        Iterator<CalcStackEntry> sei = stackEntries.iterator();
        buffer = ByteBuffer.allocate(8192);
        while(sei.hasNext()) {
            CalcStackEntry se = sei.next();
            se.addTo(buffer);
        }
        bufferLength = buffer.position();
        buffer.flip();
        for(int i=0; i<bufferLength; i++) {
            stack.add(buffer.get());
        }
    }

    public int getNumEntries() {
        //Add one for the non existant terminator (MR88 needs it)
        return stackEntries.size() + 1;
    }

    public void add(CalcStackEntry cse) {
        stackEntries.add(cse);
    }

}

// void setData(unsigned char * ptr, size_t cbLen)
// {
// m_ptr = ptr;
// m_cbLen = cbLen;
// m_pEnd = ptr + cbLen - 1;

// //so we have a handle to the data read...
// }
