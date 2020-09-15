/*
 * AET
 *
 * Copyright (C) 2020 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.aet.accessibility.report.writers

import com.cognifide.aet.accessibility.report.models.ReportRow
import com.cognifide.aet.accessibility.report.service.AccessibilityReportService
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

class PlainFileWriter(private val task: AccessibilityReportService.ServiceTask) : BaseFileWriter {
  private val stream = ByteArrayOutputStream()
  private val writer = OutputStreamWriter(stream)

  override fun writeHeader() {
    writer.write(
        """
          Company: ${task.dbKey.company}
          Project: ${task.dbKey.project}
          Suite: ${task.correlationId}
          
          """.trimIndent())
  }

  override fun writeCodeHeader(code: String) {
    writer.write("************************************************************************\n$code")
  }

  override fun writeMessage(message: String) {
    writer.write("- *$message*\n\n")
  }

  override fun writeRow(reportRow: ReportRow) {
    writer.write(
        """
          Path: [${reportRow.path}|${reportRow.url}]
          Line number: ${reportRow.lineNumber}
          {code:html}
          ${reportRow.snippet}
          {code}
          Suggested fix technique: ${reportRow.solutions}
          
          
          """.trimIndent())
  }

  override fun writeSolutions(solutions: String) {
    writer.write(
        """
          More details at: https://www.w3.org/TR/WCAG20/#meaning-doc-lang-id
          Suggested fix technique: (list of techniques - https://www.w3.org/TR/WCAG20-TECHS/)
          $solutions
          
          
          
          """.trimIndent())
  }

  override fun writeIssueSeparator() {
    writer.write("\n")
  }

  override fun toByteArray(): ByteArray = stream.toByteArray()
}
