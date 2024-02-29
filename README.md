The TSV analyzer is designed to process TSV files and provide valuable insights.</br>
It consists of two key methods:
<hr>
<ol>
  <li>
    <h3>readTsvFile():</h3>
    This method reads the TSV file and returns an object called "tsvDetails."<br>
    The "tsvDetails" object encapsulates three important pieces of information:<br> 
    <ul>
      <li>The total number of records in the file</li>
      <li>The total price calculated from the TSV data</li>
      <li>List of any duplicate values found in the file based on accountNumber and price</li>
    </ul>
  </li>
  <li>
    <h3>saveFile():</h3>
    This method allows you to save the TSV file. However, it includes an important error handling mechanism. If the TSV file contains any duplicate values, it throws an error to prevent the saving process.
  </li>
</ol>

> :warning: **IMPORTANT**
> 
> This api only works with TSV files with a specific format.
> here's the format:
>
> [AccountName]    [AccountNumber]    [Price( preferably double )]

